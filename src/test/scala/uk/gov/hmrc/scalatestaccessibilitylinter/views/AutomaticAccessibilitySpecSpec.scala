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

package uk.gov.hmrc.scalatestaccessibilitylinter.views

import org.mockito.ArgumentCaptor
import org.scalacheck.Arbitrary
import org.scalatest.matchers.MatchResult
import play.twirl.api.Html
import uk.gov.hmrc.scalatestaccessibilitylinter.views.html.{InternalErrorPage, Layout}
import org.mockito.Mockito._
import org.scalatest.events.{Event, TestPending, TestSucceeded}
import org.scalatest.{Args, Reporter}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.scalatestaccessibilitylinter.AccessibilityMatchers

class AutomaticAccessibilitySpecSpec extends AnyWordSpec with Matchers with AccessibilityMatchers with MockitoSugar {

  // stolen from org.scalatest.SharedHelpers
  class EventRecordingReporter extends Reporter {
    private var eventList: List[Event] = List()

    def apply(event: Event): Unit =
      synchronized {
        eventList ::= event
      }

    def eventsReceived: List[Event] = synchronized {
      eventList.reverse
    }
  }

  val capturedPage = ArgumentCaptor.forClass(classOf[String])

  class ExampleSpec extends AutomaticAccessibilitySpec {

    override val passAccessibilityChecks: PassAccessibilityChecksMatcher = mock[PassAccessibilityChecksMatcher]
    when(passAccessibilityChecks.apply(capturedPage.capture())).thenReturn(new MatchResult(true, "", ""))

    override def viewPackageName: String      = "uk.gov.hmrc.scalatestaccessibilitylinter.views.html"
    override def layoutClasses: Seq[Class[_]] = Seq(classOf[Layout])

    val appConfig: AppConfig                     = app.injector.instanceOf[AppConfig]
    implicit val arbConfig: Arbitrary[AppConfig] = fixed(appConfig)

    override def renderViewByClass: PartialFunction[Any, Html] = { case internalErrorPage: InternalErrorPage =>
      render(internalErrorPage)
    }

    runAccessibilityTests()
  }

  "AutomaticAccessibilitySpec" should {
    val spec     = new ExampleSpec
    val reporter = new EventRecordingReporter

    spec.run(None, Args(reporter))

    "test views that are wired up in renderViewByClass" in {
      val passedTests = reporter.eventsReceived collect { case event: TestSucceeded => event }
      passedTests.length        should be(1)
      passedTests.head.testName should be(
        "uk.gov.hmrc.scalatestaccessibilitylinter.views.html.InternalErrorPage should be accessible"
      )
      capturedPage.getValue     should include("deskpro.error.page.heading")
    }

    "mark other views as pending tests" in {
      val pendingTests = reporter.eventsReceived collect { case event: TestPending => event }
      pendingTests.length        should be(1)
      pendingTests.head.testName should be(
        "uk.gov.hmrc.scalatestaccessibilitylinter.views.html.FeedbackConfirmationPage should be accessible"
      )
    }
  }
}
