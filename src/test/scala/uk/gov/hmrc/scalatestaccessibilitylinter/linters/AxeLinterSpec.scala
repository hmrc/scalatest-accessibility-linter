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

import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import uk.gov.hmrc.scalatestaccessibilitylinter.domain._

class AxeLinterSpec extends AnyFeatureSpec with Matchers {

  Feature("return report that passed accessibility checks") {
    Scenario("axe found no violations") {
      val givenAxeReturnsNoViolations = (_: String) => "[]"

      new AxeLinter(
        givenAxeReturnsNoViolations,
        KnownIssues.empty
      ).check("<html>") shouldBe PassedAccessibilityChecks(AccessibilityLinter.axe)
    }
  }

  Feature("return report that failed accessibility checks") {
    Scenario("axe found violations") {
      val givenAxeReturnsOneViolation = (_: String) => """[
        |{
        |  "helpUrl": "https://example.com",
        |  "id": "deprecated",
        |  "impact": "serious",
        |  "description": "this comes from the description of the alert",
        |  "nodes": [
        |    {
        |      "failureSummary": "this feature is no longer recommended",
        |      "html": "<marquee>",
        |      "impact": "serious"
        |    }
        |  ]
        |}
        |]""".stripMargin

      new AxeLinter(
        givenAxeReturnsOneViolation,
        KnownIssues.empty
      ).check("anything because collaborator gives fixed response") shouldBe FailedAccessibilityChecks(
        AccessibilityLinter.axe,
        foundViolations = List(
          AccessibilityViolation(
            AccessibilityLinter.axe,
            code = "deprecated",
            severity = "serious",
            alertLevel = "ERROR",
            description = "this comes from the description of the alert",
            snippet = "<marquee>",
            helpUrl = "https://example.com",
            knownIssue = false,
            furtherInformation = None
          )
        )
      )
    }

    Scenario("axe found violations with extra unexpected properties") {
      val givenAxeReturnsOneViolation = (_: String) => """[
        |{
        |  "helpUrl": "https://example.com",
        |  "id": "deprecated",
        |  "unexpected": "data",
        |  "impact": "serious",
        |  "description": "this comes from the description of the alert",
        |  "nodes": [
        |    {
        |      "failureSummary": "this feature is no longer recommended",
        |      "html": "<marquee>",
        |      "impact": "serious",
        |      "moreUnexpectedData": 1
        |    }
        |  ]
        |}
        |]""".stripMargin

      new AxeLinter(
        givenAxeReturnsOneViolation,
        KnownIssues.empty
      ).check("anything because collaborator gives fixed response") shouldBe FailedAccessibilityChecks(
        AccessibilityLinter.axe,
        foundViolations = List(
          AccessibilityViolation(
            AccessibilityLinter.axe,
            code = "deprecated",
            severity = "serious",
            alertLevel = "ERROR",
            description = "this comes from the description of the alert",
            snippet = "<marquee>",
            helpUrl = "https://example.com",
            knownIssue = false,
            furtherInformation = None
          )
        )
      )
    }
  }
}
