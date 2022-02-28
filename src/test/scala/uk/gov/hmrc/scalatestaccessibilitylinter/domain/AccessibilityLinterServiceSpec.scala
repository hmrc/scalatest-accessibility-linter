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

package uk.gov.hmrc.scalatestaccessibilitylinter.domain

import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import uk.gov.hmrc.scalatestaccessibilitylinter.domain.AccessibilityLinter.axe

class AccessibilityLinterServiceSpec extends AnyFeatureSpec with Matchers {
  Feature("checks html and reports passed accessibility checks") {
    Scenario("no violations found") {
      val input  = "html to test"
      val linter = new AccessibilityLinter.Service(axe, KnownIssues.empty) {
        override protected def findViolations(html: String): List[AccessibilityViolation] = {
          html shouldBe input
          List.empty
        }
      }
      linter.check(input) shouldBe PassedAccessibilityChecks(axe)
    }

    Scenario("only known filtered violations found") {
      val input  = "html to test"
      val linter = new AccessibilityLinter.Service(
        axe,
        KnownIssues(
          KnownIssue(
            AccessibilityLinter.axe,
            descriptionRegex = """.*""".r,
            snippetRegex = ".*".r,
            ActionsWhenMatched(
              hideViolation = true
            )
          )
        )
      ) {
        override protected def findViolations(html: String): List[AccessibilityViolation] = List(
          AccessibilityViolation(
            axe,
            code = "deprecated",
            severity = "serious",
            alertLevel = "ERROR",
            description = "this feature is no longer recommended",
            snippet = "<marquee>",
            helpUrl = "https://example.com",
            knownIssue = false,
            furtherInformation = None
          )
        )
      }
      linter.check(input) shouldBe PassedAccessibilityChecks(axe)
    }
  }

  Feature("checks html and reports failed accessibility checks") {
    Scenario("only unknown violations found") {
      val input  = "html to test"
      val linter = new AccessibilityLinter.Service(axe, KnownIssues.empty) {
        override protected def findViolations(html: String): List[AccessibilityViolation] = List(
          AccessibilityViolation(
            axe,
            code = "deprecated",
            severity = "serious",
            alertLevel = "ERROR",
            description = "this feature is no longer recommended",
            snippet = "<marquee>",
            helpUrl = "https://example.com",
            knownIssue = false,
            furtherInformation = None
          )
        )
      }
      linter.check(input) shouldBe FailedAccessibilityChecks(
        axe,
        List(
          AccessibilityViolation(
            axe,
            code = "deprecated",
            severity = "serious",
            alertLevel = "ERROR",
            description = "this feature is no longer recommended",
            snippet = "<marquee>",
            helpUrl = "https://example.com",
            knownIssue = false,
            furtherInformation = None
          )
        )
      )
    }
  }

  Feature("updates known issues") {
    Scenario("multiple known and unknown issues found") {
      val input  = "html to test"
      val linter = new AccessibilityLinter.Service(
        axe,
        KnownIssues(
          KnownIssue(
            AccessibilityLinter.axe,
            descriptionRegex = """first""".r,
            snippetRegex = ".*".r,
            ActionsWhenMatched(
              setAlertLevel = Some("WARNING"),
              markAsKnownIssue = true,
              addFurtherInformation = Some("extra info")
            )
          )
        )
      ) {
        override protected def findViolations(html: String): List[AccessibilityViolation] = List(
          AccessibilityViolation(
            axe,
            code = "deprecated",
            severity = "serious",
            alertLevel = "ERROR",
            description = "first",
            snippet = "<marquee>",
            helpUrl = "https://example.com",
            knownIssue = false,
            furtherInformation = None
          ),
          AccessibilityViolation(
            axe,
            code = "deprecated",
            severity = "serious",
            alertLevel = "ERROR",
            description = "second",
            snippet = "<marquee>",
            helpUrl = "https://example.com",
            knownIssue = false,
            furtherInformation = None
          ),
          AccessibilityViolation(
            axe,
            code = "deprecated",
            severity = "serious",
            alertLevel = "ERROR",
            description = "first",
            snippet = "<marquee>",
            helpUrl = "https://example.com",
            knownIssue = false,
            furtherInformation = None
          )
        )
      }
      linter.check(input) shouldBe FailedAccessibilityChecks(
        axe,
        List(
          AccessibilityViolation(
            axe,
            code = "deprecated",
            severity = "serious",
            alertLevel = "WARNING",
            description = "first",
            snippet = "<marquee>",
            helpUrl = "https://example.com",
            knownIssue = true,
            furtherInformation = Some("extra info")
          ),
          AccessibilityViolation(
            axe,
            code = "deprecated",
            severity = "serious",
            alertLevel = "ERROR",
            description = "second",
            snippet = "<marquee>",
            helpUrl = "https://example.com",
            knownIssue = false,
            furtherInformation = None
          ),
          AccessibilityViolation(
            axe,
            code = "deprecated",
            severity = "serious",
            alertLevel = "WARNING",
            description = "first",
            snippet = "<marquee>",
            helpUrl = "https://example.com",
            knownIssue = true,
            furtherInformation = Some("extra info")
          )
        )
      )
    }
  }
}
