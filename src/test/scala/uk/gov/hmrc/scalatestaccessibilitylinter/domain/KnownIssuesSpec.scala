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

import com.softwaremill.diffx.Diff
import com.softwaremill.diffx.generic.auto._
import com.softwaremill.diffx.instances.DiffForString
import com.softwaremill.diffx.scalatest.DiffMatcher
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import uk.gov.hmrc.scalatestaccessibilitylinter.config.knownIssues
import uk.gov.hmrc.scalatestaccessibilitylinter.domain.AccessibilityLinter.{axe, vnu}

import scala.util.matching.Regex

class KnownIssuesSpec extends AnyFeatureSpec with Matchers with DiffMatcher {

  // in general regexes need to be compared by their string representation this
  // implicit is for the diffx library which gives us pretty case class diffs
  implicit val diffForRegex: Diff[Regex] = (left: Regex, right: Regex, _) =>
    new DiffForString().apply(left.regex, right.regex)

  val accessibilityViolation: AccessibilityViolation = AccessibilityViolation(
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

  Feature("can load known issues successfully from config") {
    Scenario("uk/gov/hmrc/scalatestaccessibilitylinter/accessibility-linter.conf") {
      knownIssues should matchTo(
        KnownIssues(
          KnownIssue(
            axe,
            descriptionRegex = """Start tag seen without seeing a doctype first\. Expected “<!DOCTYPE html>”""".r,
            snippetRegex = ".*".r,
            ActionsWhenMatched(
              hideViolation = true,
              addFurtherInformation = None,
              setAlertLevel = None
            )
          ),
          KnownIssue(
            vnu,
            descriptionRegex =
              """Attribute “readonly” is only allowed when the input type is “date”, “datetime-local”, “email”, “month”, “number”, “password”, “search”, “tel”, “text”, “time”, “url”, or “week”\.""".r,
            snippetRegex = """.*<input.*(type="hidden".*readonly=""|readonly="".*type="hidden"){1}.*>""".r,
            ActionsWhenMatched(
              markAsKnownIssue = true,
              addFurtherInformation = Some(
                "While this is a valid finding, readonly attributes on input tags of type 'hidden' have no effect on the page usability or accessibility."
              ),
              setAlertLevel = Some("INFO")
            )
          )
        )
      )
    }
  }

  Feature("filtering returns violations unchanged") {
    Scenario("known issues is empty") {
      KnownIssues.empty.filterAndUpdate(
        accessibilityViolation
      ) should matchTo(
        List(accessibilityViolation)
      )
    }

    Scenario("no matches to known issues") {
      knownIssues.filterAndUpdate(
        accessibilityViolation
      ) should matchTo(
        List(accessibilityViolation)
      )
    }
  }

  Feature("filtering returns violations with those matching known issues that should be hidden omitted") {
    Scenario("one violation") {
      KnownIssues(
        KnownIssue(
          axe,
          descriptionRegex = """.*""".r,
          snippetRegex = ".*".r,
          ActionsWhenMatched(
            hideViolation = true
          )
        )
      ).filterAndUpdate(
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
      ) shouldBe List.empty
    }

    Scenario("multiple violations") {
      KnownIssues(
        KnownIssue(
          axe,
          descriptionRegex = """first""".r,
          snippetRegex = ".*".r,
          ActionsWhenMatched(
            hideViolation = true
          )
        ),
        KnownIssue(
          axe,
          descriptionRegex = """third""".r,
          snippetRegex = ".*".r,
          ActionsWhenMatched(
            hideViolation = true
          )
        )
      ).filterAndUpdate(
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
          description = "third",
          snippet = "<marquee>",
          helpUrl = "https://example.com",
          knownIssue = false,
          furtherInformation = None
        )
      ) shouldBe List(
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
        )
      )
    }
  }

  Feature("filtering returns violations that match known issues updated with extra info") {
    Scenario("one violation") {
      KnownIssues(
        KnownIssue(
          axe,
          descriptionRegex = """.*""".r,
          snippetRegex = ".*".r,
          ActionsWhenMatched(
            setAlertLevel = Some("WARNING"),
            markAsKnownIssue = true,
            addFurtherInformation = Some("extra info")
          )
        )
      ).filterAndUpdate(
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
      ) shouldBe List(
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
    }
  }

  Feature("known issue will not match") {
    Scenario("tool is different") {
      val givenShouldMatchAnyAxeIssue = KnownIssue(
        axe,
        descriptionRegex = """.*""".r,
        snippetRegex = """.*""".r,
        ActionsWhenMatched()
      )

      givenShouldMatchAnyAxeIssue.matches(accessibilityViolation.copy(vnu)) shouldBe false
      givenShouldMatchAnyAxeIssue.matches(accessibilityViolation.copy(axe)) shouldBe true
    }

    Scenario("only one regex matches") {
      val knownIssue = KnownIssue(
        axe,
        descriptionRegex = """does match""".r,
        snippetRegex = """does match""".r,
        ActionsWhenMatched()
      )

      knownIssue.matches(
        accessibilityViolation.copy(axe, description = "does not match", snippet = "does match")
      ) shouldBe false

      knownIssue.matches(
        accessibilityViolation.copy(axe, description = "does match", snippet = "does not match")
      ) shouldBe false

      knownIssue.matches(
        accessibilityViolation.copy(axe, description = "does match", snippet = "does match")
      ) shouldBe true
    }
  }
}
