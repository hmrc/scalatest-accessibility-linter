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

import org.scalactic.source.Position
import org.scalatest.matchers.MatchResult
import org.scalatest.matchers.should.Matchers
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.{Informer, Informing}
import uk.gov.hmrc.scalatestaccessibilitylinter.domain._

import scala.collection.mutable.ListBuffer

class AccessibilityMatchersSpec extends AnyFeatureSpec with Matchers {

  Feature("tests can pass") {
    Scenario("if no linters defined") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters = Seq()
        passAccessibilityChecksGivenAnyHtml shouldBe checksPassed
        infoMessages                        shouldBe List()
      }
    }

    Scenario("if no accessibility violations returned") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters = Seq(fakeAxeLinter())
        passAccessibilityChecksGivenAnyHtml shouldBe checksPassed
        infoMessages                        shouldBe List("axe found no problems.")
      }
    }

    Scenario("if accessibility violations are known issues") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters = Seq(fakeAxeLinter(knownAccessibilityViolation.copy(alertLevel = "ERROR")))
        passAccessibilityChecksGivenAnyHtml shouldBe checksPassed
        infoMessages                        shouldBe List(
          """axe found no errors."""
        )
      }
    }

    Scenario("if accessibility violations are only warnings") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters =
          Seq(
            fakeAxeLinter(
              unknownAccessibilityViolation
                .copy(alertLevel = "WARNING", conciseDescription = Some("concise description of the issue"))
            )
          )
        passAccessibilityChecksGivenAnyHtml shouldBe checksPassed
        infoMessages                        shouldBe List(
          """axe found no errors."""
        )
      }
    }
  }

  Feature("tests can fail") {
    Scenario("only unknown accessibility violations") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters = Seq(fakeAxeLinter(unknownAccessibilityViolation))
        passAccessibilityChecksGivenAnyHtml shouldBe checksFailed
        infoMessages                        shouldBe List(
          """axe found 1 potential problem(s):""",
          """{"level":"ERROR","description":"this feature is no longer recommended","snippet":"<marquee>","helpUrl":"https://example.com","furtherInfo":""}"""
        )
      }
    }

    Scenario("first linter returns no violations and other an unknown") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters = Seq(
          fakeAxeLinter(),
          fakeVnuLinter(unknownAccessibilityViolation)
        )
        passAccessibilityChecksGivenAnyHtml shouldBe checksFailed
        infoMessages                        shouldBe List(
          """axe found no problems.""",
          """vnu found 1 potential problem(s):""",
          """{"level":"ERROR","description":"this feature is no longer recommended","snippet":"<marquee>","helpUrl":"https://example.com","furtherInfo":""}"""
        )
      }
    }

    Scenario("first linter returns known violation and second an unknown") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters = Seq(
          fakeAxeLinter(knownAccessibilityViolation.copy(alertLevel = "ERROR")),
          fakeVnuLinter(unknownAccessibilityViolation)
        )
        passAccessibilityChecksGivenAnyHtml shouldBe checksFailed
        infoMessages                        shouldBe List(
          """axe found no errors.""",
          """vnu found 1 potential problem(s):""",
          """{"level":"ERROR","description":"this feature is no longer recommended","snippet":"<marquee>","helpUrl":"https://example.com","furtherInfo":""}"""
        )
      }
    }

    Scenario("first linter returns unknown violation and second a known") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters = Seq(
          fakeAxeLinter(unknownAccessibilityViolation),
          fakeVnuLinter(knownAccessibilityViolation.copy(alertLevel = "ERROR"))
        )
        passAccessibilityChecksGivenAnyHtml shouldBe checksFailed
        infoMessages                        shouldBe List(
          """axe found 1 potential problem(s):""",
          """{"level":"ERROR","description":"this feature is no longer recommended","snippet":"<marquee>","helpUrl":"https://example.com","furtherInfo":""}""",
          """vnu found no errors."""
        )
      }
    }

    Scenario("one linter returns known then unknown violation") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters = Seq(
          fakeAxeLinter(knownAccessibilityViolation.copy(alertLevel = "ERROR"), unknownAccessibilityViolation)
        )
        passAccessibilityChecksGivenAnyHtml shouldBe checksFailed
        infoMessages                        shouldBe List(
          """axe found 1 potential problem(s):""",
          """{"level":"ERROR","description":"this feature is no longer recommended","snippet":"<marquee>","helpUrl":"https://example.com","furtherInfo":""}"""
        )
      }
    }

    Scenario("one linter returns unknown then known violation") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters =
          Seq(fakeAxeLinter(unknownAccessibilityViolation, knownAccessibilityViolation.copy(alertLevel = "ERROR")))
        passAccessibilityChecksGivenAnyHtml shouldBe checksFailed
        infoMessages                        shouldBe List(
          """axe found 1 potential problem(s):""",
          """{"level":"ERROR","description":"this feature is no longer recommended","snippet":"<marquee>","helpUrl":"https://example.com","furtherInfo":""}"""
        )
      }
    }
  }

  Feature("you can make the console output more concise") {
    Scenario("only unknown accessibility violations") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters = Seq(
          fakeAxeLinter(
            unknownAccessibilityViolation
              .copy(description = "not used for concise report", conciseDescription = Some("a concise description")),
            unknownAccessibilityViolation
              .copy(
                description = "not used for concise report",
                conciseDescription = Some("another concise description")
              )
          ),
          fakeVnuLinter(
            unknownAccessibilityViolation.copy(description = "some vnu violation", conciseDescription = None)
          )
        )
        passAccessibilityChecksWithConciseOutput shouldBe checksFailed
        infoMessages                             shouldBe List(
          """axe found 2 potential problem(s):""",
          """- a concise description""",
          """  (#target)""",
          """  https://example.com""",
          """- another concise description""",
          """  (#target)""",
          """  https://example.com""",
          """vnu found 1 potential problem(s):""",
          """- some vnu violation"""
        )
      }
    }
  }
}

trait FakeTestSuite extends Informing { this: AccessibilityMatchers =>

  def passAccessibilityChecksGivenAnyHtml: MatchResult = passAccessibilityChecks("<html is not important for tests>")

  def passAccessibilityChecksWithConciseOutput: MatchResult =
    passAccessibilityChecks(OutputFormat.Concise)("<html is not important for tests>")

  def infoMessages: Seq[String] = infoEvents.map(_._1).toList

  private val infoEvents                = new ListBuffer[(String, Option[Any])]()
  override protected def info: Informer = new Informer {
    override def apply(message: String, payload: Option[Any])(implicit pos: Position): Unit =
      infoEvents += message -> payload
  }

  def checksFailed: MatchResult =
    MatchResult(
      matches = false,
      "Accessibility violations were present.",
      "Accessibility violations were not present."
    )

  def checksPassed: MatchResult =
    MatchResult(
      matches = true,
      "Accessibility violations were present.",
      "Accessibility violations were not present."
    )

  val accessibilityViolation: AccessibilityViolation = AccessibilityViolation(
    AccessibilityLinter.axe,
    code = "deprecated",
    severity = "serious",
    alertLevel = "ERROR",
    description = "this feature is no longer recommended",
    snippet = "<marquee>",
    helpUrl = "https://example.com",
    knownIssue = false,
    furtherInformation = None,
    "#target",
    Some("concise description of the issue")
  )

  val unknownAccessibilityViolation: AccessibilityViolation = accessibilityViolation.copy(knownIssue = false)
  val knownAccessibilityViolation: AccessibilityViolation   = accessibilityViolation.copy(knownIssue = true)

  def fakeAxeLinter(violations: AccessibilityViolation*): AccessibilityLinter.Service =
    fakeLinter(AccessibilityLinter.axe, violations.toList)

  def fakeVnuLinter(violations: AccessibilityViolation*): AccessibilityLinter.Service =
    fakeLinter(AccessibilityLinter.vnu, violations.toList)

  def fakeLinter(
    linter: AccessibilityLinter,
    violations: List[AccessibilityViolation]
  ): AccessibilityLinter.Service = {
    val pLinter = linter
    new AccessibilityLinter.Service(pLinter, KnownIssues.empty) {
      override protected def findViolations(html: String): List[AccessibilityViolation] =
        violations.map(_.copy(linter = pLinter))
    }
  }

}
