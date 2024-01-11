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

package uk.gov.hmrc.scalatestaccessibilitylinter

import org.scalatest.Informing
import org.scalatest.matchers.{MatchResult, Matcher}
import uk.gov.hmrc.scalatestaccessibilitylinter.domain._

trait AccessibilityMatchers { this: Informing =>
  protected val accessibilityLinters: Seq[AccessibilityLinter.Service] =
    Seq(config.defaultAxeLinter)

  class PassAccessibilityChecksMatcher(
    accessibilityLinters: Seq[AccessibilityLinter.Service],
    outputFormat: OutputFormat = config.outputFormat
  ) extends Matcher[String] {
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
      !accessibilityReports.exists(_.hasUnknownErrors)

    private def reportResults(accessibilityReports: Seq[AccessibilityReport]): Unit =
      accessibilityReports
        .foreach {
          case report @ PassedAccessibilityChecks(linter)             =>
            info(s"$linter found no problems.", Some(report))
          case report @ FailedAccessibilityChecks(linter, violations) =>
            violations.filter(_.isUnknownError) match {
              case Nil    => info(s"$linter found no errors.", Some(report))
              case errors =>
                info(s"$linter found ${errors.size} potential problem(s):", Some(report))
                outputFormat(errors).foreach(info(_, Some(report)))
            }
        }

    private def accessibilityCheck(html: String): Seq[AccessibilityReport] =
      accessibilityLinters.map(_.check(html))
  }

  def passAccessibilityChecks = new PassAccessibilityChecksMatcher(accessibilityLinters)

  def passAccessibilityChecks(outputFormat: OutputFormat) =
    new PassAccessibilityChecksMatcher(accessibilityLinters, outputFormat)
}
