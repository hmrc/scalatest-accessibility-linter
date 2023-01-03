/*
 * Copyright 2023 HM Revenue & Customs
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

import play.api.libs.json.Json

import scala.collection.immutable.ListMap

sealed trait OutputFormat {
  def apply(violations: List[AccessibilityViolation]): List[String]
}

object OutputFormat {
  def apply(format: String): OutputFormat = format.toLowerCase() match {
    case "concise" => Concise
    case "verbose" => Verbose
  }

  object Concise extends OutputFormat {
    override def apply(violations: List[AccessibilityViolation]): List[String] =
      violations.flatMap { violation =>
        List(s"- ${shortDescription(violation)}") ++ (violation.linter match {
          case AccessibilityLinter.axe => List(s"  (${violation.cssSelector})", s"  ${violation.helpUrl}")
          case _                       => Nil
        })
      }

    private def shortDescription(violation: AccessibilityViolation) =
      violation.conciseDescription.getOrElse(violation.description)
  }

  object Verbose extends OutputFormat {
    override def apply(violations: List[AccessibilityViolation]): List[String] = violations.map(jsonLine)

    private def jsonLine(v: AccessibilityViolation): String =
      Json.stringify(
        Json
          .toJson(
            ListMap(
              "level"       -> v.alertLevel,
              "description" -> v.description,
              "snippet"     -> v.snippet,
              "helpUrl"     -> v.helpUrl,
              "furtherInfo" -> v.furtherInformation.getOrElse("")
            )
          )
      )
  }
}
