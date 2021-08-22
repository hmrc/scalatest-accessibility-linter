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

import enumeratum._

import scala.collection.immutable.IndexedSeq

sealed trait AccessibilityLinter extends EnumEntry

object AccessibilityLinter extends Enum[AccessibilityLinter] with PlayJsonEnum[AccessibilityLinter] {
  val values: IndexedSeq[AccessibilityLinter] = findValues

  case object axe extends AccessibilityLinter
  case object vnu extends AccessibilityLinter

  abstract class Service(val linter: AccessibilityLinter, knownIssues: KnownIssues) {
    def check(html: String): AccessibilityReport =
      AccessibilityReport(linter, foundViolations = knownIssues.filterAndUpdate(findViolations(html)))

    protected def findViolations(html: String): List[AccessibilityViolation]
  }
}
