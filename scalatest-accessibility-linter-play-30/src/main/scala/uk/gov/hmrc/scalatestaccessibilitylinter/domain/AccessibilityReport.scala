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

sealed trait AccessibilityReport {
  val hasUnknownErrors: Boolean
}

case class PassedAccessibilityChecks(linter: AccessibilityLinter) extends AccessibilityReport {
  val hasUnknownErrors: Boolean = false
}

case class FailedAccessibilityChecks(linter: AccessibilityLinter, foundViolations: List[AccessibilityViolation])
    extends AccessibilityReport {
  require(foundViolations.nonEmpty, "failed accessibility report must contain accessibility violations")
  val hasUnknownErrors: Boolean = foundViolations.exists(_.isUnknownError)
}

object AccessibilityReport {
  def apply(linter: AccessibilityLinter, foundViolations: List[AccessibilityViolation]): AccessibilityReport =
    if (foundViolations.isEmpty) PassedAccessibilityChecks(linter)
    else FailedAccessibilityChecks(linter, foundViolations)
}
