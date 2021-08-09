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

import org.scalatest.{Matchers, WordSpec}

class NpmProcessorSpec extends WordSpec with Matchers {

  "Given an NpmProcessor with a valid modules directory, calling process" should {

    val testDirectory =
      getClass.getClassLoader.getResource("processes").getPath

    "return an expected wrapped ProcessResult for a successful process" in {
      val processor   = NpmProcessor(testDirectory)
      val processName = "process-success.js"
      processor
        .process(processName, "") shouldBe ProcessResult(name = processName, out = "Hello World", error = "")
    }

    "return an expected wrapped ProcessResult for an error-ing process" in {
      val processor   = NpmProcessor(testDirectory)
      val processName = "process-error.js"
      processor.process(processName, "") shouldBe ProcessResult(name = processName, out = "", error = "Oh Dear")
    }

    "return an expected wrapped ProcessResult for a non-existent process" in {
      val processor     = NpmProcessor(testDirectory)
      val processName   = "process-other.js"
      val processResult = processor.process(processName, "")

      processResult.name shouldBe processName
      processResult.out  shouldBe ""
      val processErrorRegex = "Cannot find module \'(.)*/process-other.js\'".r
      processErrorRegex.findFirstMatchIn(processResult.error).isDefined shouldBe true
    }
  }
}
