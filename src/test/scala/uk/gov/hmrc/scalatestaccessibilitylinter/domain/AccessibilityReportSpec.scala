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

class AccessibilityReportSpec extends AnyFeatureSpec with Matchers {

  Feature("reports no unknown errors") {
    Scenario("passed accessibility checks") {
      PassedAccessibilityChecks(axe).hasUnknownErrors shouldBe false
    }

    Scenario("failed accessibility checks with known errors") {
      FailedAccessibilityChecks(
        axe,
        foundViolations = List(
          AccessibilityViolation(
            axe,
            code = "deprecated",
            severity = "serious",
            alertLevel = "ERROR",
            description = "this feature is no longer recommended",
            snippet = "<marquee>",
            helpUrl = "https://example.com",
            knownIssue = true,
            furtherInformation = None,
            cssSelector = ".cssSelector",
            conciseDescription = Some("concise description of the issue")
          )
        )
      ).hasUnknownErrors shouldBe false
    }

    Scenario("failed accessibility checks with warnings") {
      FailedAccessibilityChecks(
        axe,
        foundViolations = List(
          AccessibilityViolation(
            axe,
            code = "deprecated",
            severity = "serious",
            alertLevel = "WARNING",
            description = "this feature is no longer recommended",
            snippet = "<marquee>",
            helpUrl = "https://example.com",
            knownIssue = false,
            furtherInformation = None,
            cssSelector = ".cssSelector",
            conciseDescription = Some("concise description of the issue")
          )
        )
      ).hasUnknownErrors shouldBe false
    }
  }

  Feature("reports unknown errors") {
    Scenario("failed accessibility checks with unknown errors") {
      FailedAccessibilityChecks(
        axe,
        foundViolations = List(
          AccessibilityViolation(
            axe,
            code = "deprecated",
            severity = "serious",
            alertLevel = "ERROR",
            description = "this feature is no longer recommended",
            snippet = "<marquee>",
            helpUrl = "https://example.com",
            knownIssue = false,
            furtherInformation = None,
            cssSelector = ".cssSelector",
            conciseDescription = Some("concise description of the issue")
          )
        )
      ).hasUnknownErrors shouldBe true
    }
  }

  Feature("constructs a passing report") {
    Scenario("when no violations") {
      AccessibilityReport(axe, List.empty) shouldBe PassedAccessibilityChecks(axe)
    }
  }

  Feature("constructs a failed report") {
    Scenario("when only known errors") {
      AccessibilityReport(
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
            knownIssue = true,
            furtherInformation = None,
            cssSelector = ".cssSelector",
            conciseDescription = Some("concise description of the issue")
          )
        )
      ) shouldBe FailedAccessibilityChecks(
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
            knownIssue = true,
            furtherInformation = None,
            cssSelector = ".cssSelector",
            conciseDescription = Some("concise description of the issue")
          )
        )
      )
    }

    Scenario("when only unknown errors") {
      AccessibilityReport(
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
            furtherInformation = None,
            cssSelector = ".cssSelector",
            conciseDescription = Some("concise description of the issue")
          )
        )
      ) shouldBe FailedAccessibilityChecks(
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
            furtherInformation = None,
            cssSelector = ".cssSelector",
            conciseDescription = Some("concise description of the issue")
          )
        )
      )
    }

    Scenario("when only warnings") {
      AccessibilityReport(
        axe,
        List(
          AccessibilityViolation(
            axe,
            code = "deprecated",
            severity = "serious",
            alertLevel = "WARNING",
            description = "this feature is no longer recommended",
            snippet = "<marquee>",
            helpUrl = "https://example.com",
            knownIssue = false,
            furtherInformation = None,
            cssSelector = ".cssSelector",
            conciseDescription = Some("concise description of the issue")
          )
        )
      ) shouldBe FailedAccessibilityChecks(
        axe,
        List(
          AccessibilityViolation(
            axe,
            code = "deprecated",
            severity = "serious",
            alertLevel = "WARNING",
            description = "this feature is no longer recommended",
            snippet = "<marquee>",
            helpUrl = "https://example.com",
            knownIssue = false,
            furtherInformation = None,
            cssSelector = ".cssSelector",
            conciseDescription = Some("concise description of the issue")
          )
        )
      )
    }
  }

}
