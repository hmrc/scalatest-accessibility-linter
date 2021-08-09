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

import org.scalatest.matchers._
import play.api.libs.json.{JsArray, JsValue, Json}

trait AccessibilityMatchers extends ProcessingMatchers {
  override protected val processes: Seq[String] = Seq("axe", "vnu")
  override protected val processor: Processor   = NpmProcessor("target/sbtaccessibilitylinter/js")
}

private[scalatestaccessibilitylinter] trait ProcessingMatchers {

  protected val processes: Seq[String]
  protected val processor: Processor

  class HaveNoAccessibilityViolationsMatcher extends Matcher[String] {
    def apply(left: String): MatchResult = {

      val processResults: Seq[ParsedProcessResult] =
        processes.map { name =>
          val processResult = processor.process(name, left)
          val violations    = Json.parse(processResult.out).as[JsArray].value
          ParsedProcessResult(processResult, violations)
        }

      val failures = processResults.filter(_.violations.nonEmpty)

      MatchResult(
        failures.isEmpty,
        failures
          .map(failure => s"${failure.processResult.name} tests failed: ${failure.processResult.out}")
          .mkString("\n"),
        "Accessibility tests succeeded"
      )
    }
  }

  case class ParsedProcessResult(processResult: ProcessResult, violations: Seq[JsValue])

  def haveNoAccessibilityViolations = new HaveNoAccessibilityViolationsMatcher
}
