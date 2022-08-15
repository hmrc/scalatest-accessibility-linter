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

package uk.gov.hmrc.scalatestaccessibilitylinter.reporters

import org.scalatest.events.{Ordinal, RecordableEvent, SuiteCompleted, TestPending}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.immutable.IndexedSeq
import java.io.ByteArrayOutputStream

class AutomaticAccessibilityReporterSpec extends AnyWordSpec with Matchers {

  private val pendingEvent = TestPending(
    new Ordinal(0),
    "someSuiteName",
    "someSuiteId",
    None,
    "foo.bar.SomeClassName should be accessible",
    "be accessible",
    IndexedSeq.empty[RecordableEvent]
  )

  private def captureStdOut[T](block: => T): String = {
    val stdout = new ByteArrayOutputStream()
    Console.withOut(stdout) {
      block
    }
    stdout.toString
  }

  "AutomaticAccessibilityReporter" should {

    "output guidance on how to implement each pending test" in {
      val reporter = new AutomaticAccessibilityReporter()

      val actualGuidance = captureStdOut {
        reporter(pendingEvent)
      }

      val expectedGuidance =
        s"""Missing wiring - add the following to your renderViewByClass function:
           |\tcase someClassName: SomeClassName => render(someClassName)
           |""".stripMargin

      actualGuidance should be(expectedGuidance)
    }

    "output complete renderViewByClass definition for all pending tests" in {
      val reporter = new AutomaticAccessibilityReporter()

      captureStdOut {
        reporter(pendingEvent.copy(testName = "foo.bar.ContactHmrcPage should be accessible"))
        reporter(pendingEvent.copy(testName = "foo.bar.ErrorPage should be accessible"))
        reporter(pendingEvent.copy(testName = "foo.bar.FeedbackPage should be accessible"))
      }

      val completedEvent = SuiteCompleted(new Ordinal(0), "someSuiteName", "someSuiteId", None)

      val actualGuidance = captureStdOut {
        reporter(completedEvent)
      }

      val expectedGuidance =
        s"""
           |All wiring for renderViewByClass below:
           |
           |override def renderViewByClass: PartialFunction[Any, Html] = {
           |\tcase contactHmrcPage: ContactHmrcPage => render(contactHmrcPage)
           |\tcase errorPage: ErrorPage => render(errorPage)
           |\tcase feedbackPage: FeedbackPage => render(feedbackPage)
           |}
           |""".stripMargin
      actualGuidance should be(expectedGuidance)
    }
  }
}
