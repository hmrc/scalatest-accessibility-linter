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

package uk.gov.hmrc.scalatestaccessibilitylinter.linters

/* This VNU validator is temporarily disabled due to the lack of new release of nu.validator library since 2020:
   https://github.com/validator/validator. Alternative solutions could include bundling the JAR into the project,
   depending on whether we decide to keep actively developing this linter. The latest release of the validator library
   is not compatible with latest Scala.
*/

import play.api.libs.functional.syntax._
import play.api.libs.json._
import uk.gov.hmrc.scalatestaccessibilitylinter.domain._

class VnuLinter(vnuProcess: String => String, knownIssues: KnownIssues)
  extends AccessibilityLinter.Service(AccessibilityLinter.vnu, knownIssues) {

  override protected def findViolations(html: String): List[AccessibilityViolation] =
    (Json.parse(vnuProcess(html)) \ "messages").as[List[VnuAlert]].map(asViolation)

  private def asViolation(vnuAlert: VnuAlert): AccessibilityViolation =
    AccessibilityViolation(
      linter,
      code = "UNDEFINED",
      severity = vnuAlert.severity,
      alertLevel = alertMapping(vnuAlert.severity),
      description = vnuAlert.message,
      snippet = vnuAlert.extract,
      helpUrl = "UNDEFINED",
      knownIssue = false,
      furtherInformation = None,
      cssSelector = "N/A",
      conciseDescription = None
    )

  private val alertMapping: Map[String, String] = Map(
    "info"     -> "INFO",
    "warning"  -> "WARNING",
    "minor"    -> "ERROR",
    "moderate" -> "ERROR",
    "serious"  -> "ERROR",
    "critical" -> "ERROR",
    "error"    -> "ERROR"
  )

  case class VnuAlert(severity: String, message: String, extract: String)
  implicit val vnuAlertReads: Reads[VnuAlert] = (
    ((JsPath \ "type").read[String] or Reads.pure("UNDEFINED")) and
      ((JsPath \ "message").read[String] or Reads.pure("UNDEFINED")) and
      ((JsPath \ "extract").read[String] or Reads.pure("UNDEFINED"))
    )(VnuAlert.apply _)

}

/* This is a dummy validator pending decision on strategy on including VNU in the linters given the above issue with the
   nu.validator library.
*/
object VnuLinter {
  def fromEmbeddedValidator(knownIssues: KnownIssues): VnuLinter = new VnuLinter(
    vnuProcess = _ => "",
    knownIssues
  )
}
