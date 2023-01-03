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

import html.Layout
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.mutable.ArrayBuffer

class ViewDiscoverySpec extends AnyWordSpec with Matchers {

  trait AutomaticAccessibilitySetupWithPages extends AutomaticAccessibilitySpec {
    override def viewPackageName: String      = "uk.gov.hmrc.scalatestaccessibilitylinter.views.html"
    override def layoutClasses: Seq[Class[_]] = Seq(classOf[Layout])
  }

  trait AutomaticAccessibilitySetupWithNoPages extends AutomaticAccessibilitySpec {
    override def viewPackageName: String      = "uk.gov.hmrc.nothing"
    override def layoutClasses: Seq[Class[_]] = Seq(classOf[Layout])
  }

  "ViewDiscovery" should {
    "should find all classes of type BaseScalaTemplate" in new AutomaticAccessibilitySetupWithPages {
      viewNames() shouldBe ArrayBuffer(
        ViewName("uk.gov.hmrc.scalatestaccessibilitylinter.views.html.FeedbackConfirmationPage"),
        ViewName("uk.gov.hmrc.scalatestaccessibilitylinter.views.html.InternalErrorPage"),
        ViewName("uk.gov.hmrc.scalatestaccessibilitylinter.views.html.VeryComplexPage")
      )
    }

    "should find no classes of type BaseScalaTemplate when none exist" in new AutomaticAccessibilitySetupWithNoPages {
      viewNames() shouldBe ArrayBuffer()
    }
  }
}
