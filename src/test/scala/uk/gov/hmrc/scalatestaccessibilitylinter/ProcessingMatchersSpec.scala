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

import org.scalatest.matchers.MatchResult
import org.scalatest.{Matchers, WordSpec}

class ProcessingMatchersSpec extends WordSpec with Matchers {

  "Given a class implementing ProcessingMatchers, using the matcher" should {

    "return successful MatchResult when single process returns no errors" in {
      val matcher = TestMatchers(Seq("axe")).haveNoAccessibilityViolations.apply("")
      matcher shouldBe MatchResult(
        matches = true,
        rawFailureMessage = "",
        rawNegatedFailureMessage = "Accessibility tests succeeded"
      )
    }

    "return successful MatchResult when multiple processes return no errors" in {
      val matcher = TestMatchers(Seq("axe", "manual")).haveNoAccessibilityViolations.apply("")
      matcher shouldBe MatchResult(
        matches = true,
        rawFailureMessage = "",
        rawNegatedFailureMessage = "Accessibility tests succeeded"
      )
    }

    "return failed MatchResult with raw JSON when single process returns errors" in {
      val matcher = TestMatchers(Seq("vnu")).haveNoAccessibilityViolations.apply("")
      matcher shouldBe MatchResult(
        matches = false,
        rawFailureMessage = """vnu tests failed: [{"someKey": "someVal"}]""",
        rawNegatedFailureMessage = "Accessibility tests succeeded"
      )
    }

    "return failed MatchResult with raw JSON when multiple process return errors" in {
      val matcher = TestMatchers(Seq("vnu", "wave")).haveNoAccessibilityViolations.apply("")
      matcher shouldBe MatchResult(
        matches = false,
        rawFailureMessage =
          "vnu tests failed: [{\"someKey\": \"someVal\"}]\nwave tests failed: [{\"someKey\": \"someVal\"}]",
        rawNegatedFailureMessage = "Accessibility tests succeeded"
      )
    }

    "return failed MatchResult with raw JSON when mix of failed and successful process returned" in {
      val matcher = TestMatchers(Seq("axe", "vnu")).haveNoAccessibilityViolations.apply("")
      matcher shouldBe MatchResult(
        matches = false,
        rawFailureMessage = "vnu tests failed: [{\"someKey\": \"someVal\"}]",
        rawNegatedFailureMessage = "Accessibility tests succeeded"
      )
    }
  }

  case class TestMatchers(override protected val processes: Seq[String]) extends ProcessingMatchers {
    override val processor: Processor = TestProcessor()
  }

  case class TestProcessor() extends Processor {
    override def process(processName: String, left: String): ProcessResult =
      processName match {
        case "axe"    => ProcessResult("axe", "[]", "")
        case "manual" => ProcessResult("manual", "[]", "")
        case other    => ProcessResult(other, "[{\"someKey\": \"someVal\"}]", "")
      }
  }
}
