/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.scalatestaccessibilitylinter.domain

import com.typesafe.config.{Config, ConfigException}

import scala.collection.JavaConverters._
import scala.util.matching.Regex

case class KnownIssues(knownIssues: List[KnownIssue]) {
  def filterAndUpdate(foundViolations: AccessibilityViolation*): List[AccessibilityViolation] =
    filterAndUpdate(foundViolations.toList)

  def filterAndUpdate(foundViolations: List[AccessibilityViolation]): List[AccessibilityViolation] =
    foundViolations.flatMap { violation =>
      checkIfKnown(violation) match {
        case None                       => Some(violation)
        case Some(actionsForKnownIssue) =>
          if (actionsForKnownIssue.hideViolation) None
          else
            Some(
              violation.copy(
                knownIssue = actionsForKnownIssue.markAsKnownIssue,
                alertLevel = actionsForKnownIssue.setAlertLevel.getOrElse(violation.alertLevel),
                furtherInformation = actionsForKnownIssue.addFurtherInformation.orElse(violation.furtherInformation)
              )
            )
      }
    }

  protected def checkIfKnown(issue: AccessibilityViolation): Option[ActionsWhenMatched] =
    knownIssues.find(_.matches(issue)).map(_.actionsWhenMatched)
}

object KnownIssues {
  val empty: KnownIssues = KnownIssues()

  def apply(knownIssues: KnownIssue*): KnownIssues = KnownIssues(knownIssues.toList)

  def fromConfigList(configList: java.util.List[_ <: Config]): KnownIssues =
    new KnownIssues(
      configList.asScala.toList
        .map { knownIssue: Config =>
          val actions = knownIssue.getConfig("action")

          KnownIssue(
            linter = AccessibilityLinter.withNameInsensitive(knownIssue.getString("tool")),
            descriptionRegex = knownIssue.getString("descriptionRegex").r,
            snippetRegex = knownIssue.getString("snippetRegex").r,
            ActionsWhenMatched(
              hideViolation = boolean(actions.getBoolean("filterGlobally")),
              markAsKnownIssue = boolean(actions.getBoolean("knownIssue")),
              addFurtherInformation = maybe[String](actions.getString("furtherInformation")),
              setAlertLevel = maybe[String](actions.getString("alertLevel"))
            )
          )
        }
    )

  protected def boolean(getConfig: => Boolean): Boolean =
    try getConfig
    catch {
      case _: ConfigException.Missing => false
    }

  protected def maybe[T](getConfig: => T): Option[T] =
    try Some(getConfig)
    catch {
      case _: ConfigException.Missing => None
    }
}

case class KnownIssue(
  linter: AccessibilityLinter,
  descriptionRegex: Regex,
  snippetRegex: Regex,
  actionsWhenMatched: ActionsWhenMatched
) {
  def matches(issue: AccessibilityViolation): Boolean = (
    issue.linter == this.linter
      && descriptionRegex.findFirstIn(issue.description).isDefined
      && snippetRegex.findFirstIn(issue.snippet).isDefined
  )
}

case class ActionsWhenMatched(
  hideViolation: Boolean = false,
  markAsKnownIssue: Boolean = false,
  addFurtherInformation: Option[String] = None,
  setAlertLevel: Option[String] = None
)
