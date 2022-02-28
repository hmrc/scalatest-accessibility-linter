/*
 * Copyright 2022 HM Revenue & Customs
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

package uk.gov.hmrc.scalatestaccessibilitylinter.linters

import org.scalatest.matchers.should.Matchers
import org.scalatest.featurespec.AnyFeatureSpec
import uk.gov.hmrc.scalatestaccessibilitylinter.domain._

class VnuLinterSpec extends AnyFeatureSpec with Matchers {

  Feature("return report that passed accessibility checks") {
    Scenario("vnu found no violations") {
      val givenVnuReturnsNoErrors = (_: String) => """{"messages":[]}"""

      new VnuLinter(
        givenVnuReturnsNoErrors,
        KnownIssues.empty
      ).check("<html>") shouldBe PassedAccessibilityChecks(AccessibilityLinter.vnu)
    }
  }

  Feature("return report that failed accessibility checks") {
    Scenario("vnu found errors") {
      val givenVnuReturnsOneError = (_: String) => """
        |{
        |  "messages":[
        |    {
        |      "type":"error",
        |      "lastLine":2,
        |      "firstLine":1,
        |      "lastColumn":16,
        |      "firstColumn":1,
        |      "message":"Start tag seen without seeing a doctype first. Expected “<!DOCTYPE html>”.",
        |      "extract":"\n<html lang=\"en\">\n<head",
        |      "hiliteStart":0,
        |      "hiliteLength":17
        |    }
        |  ]
        |}""".stripMargin

      new VnuLinter(
        givenVnuReturnsOneError,
        KnownIssues.empty
      ).check("anything because collaborator gives fixed response") shouldBe FailedAccessibilityChecks(
        AccessibilityLinter.vnu,
        foundViolations = List(
          AccessibilityViolation(
            AccessibilityLinter.vnu,
            code = "UNDEFINED",
            severity = "error",
            alertLevel = "ERROR",
            description = "Start tag seen without seeing a doctype first. Expected “<!DOCTYPE html>”.",
            snippet = "\n<html lang=\"en\">\n<head",
            helpUrl = "UNDEFINED",
            knownIssue = false,
            furtherInformation = None
          )
        )
      )
    }

    Scenario("vnu found errors with extra unexpected properties") {
      val givenVnuReturnsOneError = (_: String) => """
        |{
        |  "messages":[
        |    {
        |      "type":"error",
        |      "lastLine":2,
        |      "firstLine":1,
        |      "lastColumn":16,
        |      "firstColumn":1,
        |      "thiswasunexpected": "wow",
        |      "message":"Start tag seen without seeing a doctype first. Expected “<!DOCTYPE html>”.",
        |      "extract":"\n<html lang=\"en\">\n<head",
        |      "hiliteStart":0,
        |      "hiliteLength":17
        |    }
        |  ]
        |}""".stripMargin

      new VnuLinter(
        givenVnuReturnsOneError,
        KnownIssues.empty
      ).check("anything because collaborator gives fixed response") shouldBe FailedAccessibilityChecks(
        AccessibilityLinter.vnu,
        foundViolations = List(
          AccessibilityViolation(
            AccessibilityLinter.vnu,
            code = "UNDEFINED",
            severity = "error",
            alertLevel = "ERROR",
            description = "Start tag seen without seeing a doctype first. Expected “<!DOCTYPE html>”.",
            snippet = "\n<html lang=\"en\">\n<head",
            helpUrl = "UNDEFINED",
            knownIssue = false,
            furtherInformation = None
          )
        )
      )
    }
  }
}
