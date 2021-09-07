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

package uk.gov.hmrc.scalatestaccessibilitylinter.linters

import org.scalatest.flatspec
import org.scalatest.matchers.should.Matchers
import uk.gov.hmrc.scalatestaccessibilitylinter.domain._

class VnuLinterIntegrationSpec extends flatspec.AnyFlatSpec with Matchers {
  behavior of "VnuLinter.fromEmbeddedValidator"

  it should "return expected errors when invalid html provided" in {
    VnuLinter
      .fromEmbeddedValidator(KnownIssues.empty)
      .check(
        s"""
        |<html lang="en">
        |<head><title>test</title></head>
        |<body>
        |<main>
        |<h1>Test</h1>
        |<input type="text">
        |</main>
        |</body>
        |</html>""".stripMargin
      ) shouldBe
      FailedAccessibilityChecks(
        AccessibilityLinter.vnu,
        List(
          AccessibilityViolation(
            linter = AccessibilityLinter.vnu,
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

  it should "return no errors when valid html is provided" in {
    VnuLinter
      .fromEmbeddedValidator(KnownIssues.empty)
      .check(
        s"""
        |<!DOCTYPE html>
        |<html lang="en">
        |<head><title>test</title></head>
        |<body>
        |<main>
        |<h1>Test</h1>
        |</main>
        |</body>
        |</html>""".stripMargin
      ) shouldBe PassedAccessibilityChecks(AccessibilityLinter.vnu)
  }
}
