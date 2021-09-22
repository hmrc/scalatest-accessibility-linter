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

package uk.gov.hmrc.scalatestaccessibilitylinter

import org.scalatest.Informing
import org.scalatest.matchers.{MatchResult, Matcher}
import play.api.libs.json.Json
import uk.gov.hmrc.scalatestaccessibilitylinter.config.{defaultAxeLinter, defaultVnuLinter}
import uk.gov.hmrc.scalatestaccessibilitylinter.domain._

import scala.collection.immutable.ListMap
import scala.language.{implicitConversions, reflectiveCalls}

trait AccessibilityMatchers { this: Informing =>
  protected val accessibilityLinters: Seq[AccessibilityLinter.Service] = Seq(defaultAxeLinter, defaultVnuLinter)

  private type PlayLikeWrappedHtml = { def body: String }
  implicit def playLikeHtmlToString(html: PlayLikeWrappedHtml): String = html.body

  class PassAccessibilityChecksMatcher(accessibilityLinters: Seq[AccessibilityLinter.Service]) extends Matcher[String] {
    def apply(html: String): MatchResult = {
      val accessibilityReports = accessibilityCheck(html)

      reportResults(accessibilityReports)

      MatchResult(
        testShouldPass(accessibilityReports),
        s"Accessibility violations were present.",
        s"Accessibility violations were not present."
      )
    }

    private def testShouldPass(accessibilityReports: Seq[AccessibilityReport]): Boolean =
      accessibilityReports.forall(_.hasNoUnknownErrors)

    private def reportResults(accessibilityReports: Seq[AccessibilityReport]): Unit =
      accessibilityReports
        .foreach {
          case report @ PassedAccessibilityChecks(linter)             =>
            info(s"$linter found no problems.", Some(report))
          case report @ FailedAccessibilityChecks(linter, violations) =>
            info(s"$linter found ${violations.size} potential problem(s):", Some(report))

            violations.foreach(violation => info(jsonLine(violation)))
        }

    private def jsonLine(v: AccessibilityViolation): String =
      Json.stringify(
        Json
          .toJson(
            ListMap(
              "level"       -> (if (v.alertLevel == "ERROR" && v.knownIssue) "WARNING" else v.alertLevel),
              "description" -> v.description,
              "snippet"     -> v.snippet,
              "helpUrl"     -> v.helpUrl,
              "furtherInfo" -> v.furtherInformation.getOrElse("")
            )
          )
      )

    private def accessibilityCheck(html: String): Seq[AccessibilityReport] =
      accessibilityLinters.map(_.check(html))
  }

  def passAccessibilityChecks = new PassAccessibilityChecksMatcher(accessibilityLinters)
}
