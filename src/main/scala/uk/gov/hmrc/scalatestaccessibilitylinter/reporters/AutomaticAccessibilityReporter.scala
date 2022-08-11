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

import org.scalatest.Reporter
import org.scalatest.events.{Event, SuiteCompleted, TestPending}

import scala.collection.mutable.ListBuffer

class AutomaticAccessibilityReporter() extends Reporter {
  var infos: ListBuffer[String] = new ListBuffer[String]()

  override def apply(event: Event): Unit = event match {
    case event: TestPending =>
      val className     = event.testName
        .replace(s"should ${event.testText}", "")
        .trim
        .split("\\.")
        .last
      val classInstance = className.head.toLower + className.tail
      val caseCode      = s"case $classInstance: $className => render($classInstance)"
      println(
        "Missing wiring - add the following to your renderViewByClass function:\n" +
          s"\t$caseCode"
      )

      infos += caseCode
    case _: SuiteCompleted  =>
      println(s"\nAll wiring for renderViewByClass below:\n")
      println("override def renderViewByClass: PartialFunction[Any, Html] = {")
      infos.toList.foreach(c => println("\t" + c))
      println("}")
    case _                  =>
  }
}
