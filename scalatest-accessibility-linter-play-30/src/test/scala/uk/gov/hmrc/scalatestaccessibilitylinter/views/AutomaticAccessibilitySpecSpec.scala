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

package uk.gov.hmrc.scalatestaccessibilitylinter.views

import org.scalacheck.Arbitrary
import play.twirl.api.Html
import org.scalatest.events.{Event, TestFailed, TestPending, TestSucceeded}
import org.scalatest.{Args, Reporter}
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.MatchResult
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.hmrc.scalatestaccessibilitylinter.AccessibilityMatchers
import uk.gov.hmrc.scalatestaccessibilitylinter.views.html.{InternalErrorPage, Layout, VeryComplexPage}

import java.util.concurrent.atomic.AtomicReference

class AutomaticAccessibilitySpecSpec extends AnyWordSpec with Matchers with AccessibilityMatchers {

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

  private val capturedViews = new AtomicReference(Seq.empty[String])

  class ExampleSpec extends AutomaticAccessibilitySpec {

    override val passAccessibilityChecks: PassAccessibilityChecksMatcher =
      new PassAccessibilityChecksMatcher(accessibilityLinters = Seq.empty) {
        override def apply(html: String): MatchResult = {
          capturedViews.updateAndGet(_ :+ html)
          new MatchResult(true, "", "")
        }
      }

    override def viewPackageName: String      = "uk.gov.hmrc.scalatestaccessibilitylinter.views.html"
    override def layoutClasses: Seq[Class[_]] = Seq(classOf[Layout])

    val appConfig: AppConfig                     = app.injector.instanceOf[AppConfig]
    implicit val arbConfig: Arbitrary[AppConfig] = fixed(appConfig)

    override def renderViewByClass: PartialFunction[Any, Html] = {
      case internalErrorPage: InternalErrorPage => render(internalErrorPage)
      case veryComplexPage: VeryComplexPage     => render(veryComplexPage)
    }

    runAccessibilityTests()
  }

  "AutomaticAccessibilitySpec" should {
    val spec     = new ExampleSpec
    val reporter = new EventRecordingReporter

    spec.run(None, Args(reporter))

    def checkTestsPassed(pages: String*): Unit = {
      val tests = reporter.eventsReceived.collect {
        case event: TestSucceeded => (event.testName, "Success")
        case event: TestFailed    => (event.testName, event.message + event.location.fold("")(" location: " + _))
      }
      tests shouldBe pages.map(p => (p + " should be accessible" -> "Success"))
    }

    "test views that are wired up in renderViewByClass" in {
      checkTestsPassed(
        "uk.gov.hmrc.scalatestaccessibilitylinter.views.html.InternalErrorPage",
        "uk.gov.hmrc.scalatestaccessibilitylinter.views.html.VeryComplexPage"
      )

      val htmlTag      = "<[^>]+>"
      val pageHeadings = capturedViews
        .get()
        .flatMap(
          _.split("\\n")
            .filter(_.contains("<h1"))
            .map(_.replaceAll(htmlTag, "").trim)
        )
      pageHeadings should be(
        Seq("Internal Error Page", "Very Complex Page")
      )
    }

    "ensure unicode characters are removed from views that have been rendered" in {
      checkTestsPassed(
        "uk.gov.hmrc.scalatestaccessibilitylinter.views.html.InternalErrorPage",
        "uk.gov.hmrc.scalatestaccessibilitylinter.views.html.VeryComplexPage"
      )

      val unicodeCharactersRegex = "[^\\p{ASCII}]&[^\\p{IsLatin}]".r
      val unicodeCharacters      = capturedViews.get().map(unicodeCharactersRegex.findFirstMatchIn(_))
      unicodeCharacters shouldEqual Seq(None, None)
    }

    "mark other views as pending tests" in {
      val pendingTests = reporter.eventsReceived collect { case event: TestPending => event }
      pendingTests.map(_.testName) should be(
        Seq(
          "uk.gov.hmrc.scalatestaccessibilitylinter.views.html.FeedbackConfirmationPage should be accessible"
        )
      )
    }
  }
}
