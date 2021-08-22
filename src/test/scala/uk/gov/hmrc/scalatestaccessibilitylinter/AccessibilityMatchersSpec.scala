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

import org.scalactic.source.Position
import org.scalatest.matchers.MatchResult
import org.scalatest.matchers.should.Matchers
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.{Informer, Informing}
import uk.gov.hmrc.scalatestaccessibilitylinter.domain._

import scala.collection.mutable.ListBuffer

class AccessibilityMatchersSpec extends AnyFeatureSpec with Matchers {

  Feature("tests can pass") {
    Scenario("no linters defined") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters = Seq()
        passAccessibilityChecksGivenAnyHtml shouldBe checksPassed
        infoMessages                        shouldBe List()
      }
    }

    Scenario("no accessibility violations returned") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters = Seq(fakeAxeLinter())
        passAccessibilityChecksGivenAnyHtml shouldBe checksPassed
        infoMessages                        shouldBe List("axe found no problems.")
      }
    }

    Scenario("only known accessibility violations returned") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters = Seq(fakeAxeLinter(knownAccessibilityViolation))
        passAccessibilityChecksGivenAnyHtml shouldBe checksPassed
        infoMessages                        shouldBe List(
          """axe found 1 potential problem(s):""",
          """{"level":"WARNING","description":"this feature is no longer recommended","snippet":"<marquee>","helpUrl":"https://example.com","furtherInfo":""}"""
        )
      }
    }

    Scenario("only warnings returned") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters =
          Seq(fakeAxeLinter(unknownAccessibilityViolation.copy(alertLevel = "WARNING")))
        passAccessibilityChecksGivenAnyHtml shouldBe checksPassed
        infoMessages                        shouldBe List(
          """axe found 1 potential problem(s):""",
          """{"level":"WARNING","description":"this feature is no longer recommended","snippet":"<marquee>","helpUrl":"https://example.com","furtherInfo":""}"""
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
          fakeAxeLinter(knownAccessibilityViolation),
          fakeVnuLinter(unknownAccessibilityViolation)
        )
        passAccessibilityChecksGivenAnyHtml shouldBe checksFailed
        infoMessages                        shouldBe List(
          """axe found 1 potential problem(s):""",
          """{"level":"WARNING","description":"this feature is no longer recommended","snippet":"<marquee>","helpUrl":"https://example.com","furtherInfo":""}""",
          """vnu found 1 potential problem(s):""",
          """{"level":"ERROR","description":"this feature is no longer recommended","snippet":"<marquee>","helpUrl":"https://example.com","furtherInfo":""}"""
        )
      }
    }

    Scenario("first linter returns unknown violation and second a known") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters = Seq(
          fakeAxeLinter(unknownAccessibilityViolation),
          fakeVnuLinter(knownAccessibilityViolation)
        )
        passAccessibilityChecksGivenAnyHtml shouldBe checksFailed
        infoMessages                        shouldBe List(
          """axe found 1 potential problem(s):""",
          """{"level":"ERROR","description":"this feature is no longer recommended","snippet":"<marquee>","helpUrl":"https://example.com","furtherInfo":""}""",
          """vnu found 1 potential problem(s):""",
          """{"level":"WARNING","description":"this feature is no longer recommended","snippet":"<marquee>","helpUrl":"https://example.com","furtherInfo":""}"""
        )
      }
    }

    Scenario("one linter returns known then unknown violation") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters = Seq(
          fakeAxeLinter(knownAccessibilityViolation, unknownAccessibilityViolation)
        )
        passAccessibilityChecksGivenAnyHtml shouldBe checksFailed
        infoMessages                        shouldBe List(
          """axe found 2 potential problem(s):""",
          """{"level":"WARNING","description":"this feature is no longer recommended","snippet":"<marquee>","helpUrl":"https://example.com","furtherInfo":""}""",
          """{"level":"ERROR","description":"this feature is no longer recommended","snippet":"<marquee>","helpUrl":"https://example.com","furtherInfo":""}"""
        )
      }
    }

    Scenario("one linter returns unknown then known violation") {
      new FakeTestSuite with AccessibilityMatchers {
        override val accessibilityLinters =
          Seq(fakeAxeLinter(unknownAccessibilityViolation, knownAccessibilityViolation))
        passAccessibilityChecksGivenAnyHtml shouldBe checksFailed
        infoMessages                        shouldBe List(
          """axe found 2 potential problem(s):""",
          """{"level":"ERROR","description":"this feature is no longer recommended","snippet":"<marquee>","helpUrl":"https://example.com","furtherInfo":""}""",
          """{"level":"WARNING","description":"this feature is no longer recommended","snippet":"<marquee>","helpUrl":"https://example.com","furtherInfo":""}"""
        )
      }
    }
  }

}

trait FakeTestSuite extends Informing { this: AccessibilityMatchers =>

  def passAccessibilityChecksGivenAnyHtml: MatchResult = passAccessibilityChecks("<html is not important for tests>")

  def infoMessages: Seq[String]         = infoEvents.map(_._1).toList
  def infoPayloads: Seq[Option[Any]]    = infoEvents.map(_._2).toList
  private var infoEvents                = new ListBuffer[(String, Option[Any])]()
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
    furtherInformation = None
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
  ): AccessibilityLinter.Service =
    new AccessibilityLinter.Service(linter, KnownIssues.empty) {
      override protected def findViolations(html: String): List[AccessibilityViolation] = violations.map(_.copy(linter))
    }

}
