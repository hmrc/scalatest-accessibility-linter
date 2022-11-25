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
import uk.gov.hmrc.scalatestaccessibilitylinter.views.html.{InternalErrorPage, Layout, VeryComplexPage}
import org.mockito.Mockito._
import org.scalatest.events.{Event, TestPending, TestSucceeded}
import org.scalatest.{Args, Reporter}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.scalatestaccessibilitylinter.AccessibilityMatchers
import scala.collection.JavaConverters._

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

  val viewCaptor = ArgumentCaptor.forClass(classOf[String])

  class ExampleSpec extends AutomaticAccessibilitySpec {

    override val passAccessibilityChecks: PassAccessibilityChecksMatcher = mock[PassAccessibilityChecksMatcher]
    when(passAccessibilityChecks.apply(viewCaptor.capture())).thenReturn(new MatchResult(true, "", ""))

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

    "test views that are wired up in renderViewByClass" in {
      val passedTests = reporter.eventsReceived collect { case event: TestSucceeded => event }
      passedTests.map(_.testName) should be(
        Seq(
          "uk.gov.hmrc.scalatestaccessibilitylinter.views.html.InternalErrorPage should be accessible",
          "uk.gov.hmrc.scalatestaccessibilitylinter.views.html.VeryComplexPage should be accessible"
        )
      )

      val htmlTag      = "<[^>]+>"
      val testedViews  = viewCaptor.getAllValues.asScala.toList
      val pageHeadings = testedViews.flatMap(
        _.split("\\n")
          .filter(_.contains("<h1"))
          .map(_.replaceAll(htmlTag, "").trim)
      )
      pageHeadings should be(
        Seq("Internal Error Page", "Very Complex Page")
      )
    }

    "ensure unicode characters are removed from views that have been rendered" in {
      val passedTests = reporter.eventsReceived collect { case event: TestSucceeded => event }
      passedTests.map(_.testName) should be(
        Seq(
          "uk.gov.hmrc.scalatestaccessibilitylinter.views.html.InternalErrorPage should be accessible",
          "uk.gov.hmrc.scalatestaccessibilitylinter.views.html.VeryComplexPage should be accessible"
        )
      )

      val testedViews            = viewCaptor.getAllValues.asScala.toList
      val unicodeCharactersRegex = "[^\\p{ASCII}]&[^\\p{IsLatin}]".r
      val unicodeCharacters      = testedViews.map(unicodeCharactersRegex.findFirstMatchIn(_))
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
