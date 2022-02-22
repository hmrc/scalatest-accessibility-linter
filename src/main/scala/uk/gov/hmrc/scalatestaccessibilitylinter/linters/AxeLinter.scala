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

package uk.gov.hmrc.scalatestaccessibilitylinter.linters

import play.api.libs.functional.syntax._
import play.api.libs.json._
import uk.gov.hmrc.scalatestaccessibilitylinter.domain._

import java.io.{ByteArrayInputStream, File}
import scala.sys.process.Process

class AxeLinter(axeProcess: String => String, knownIssues: KnownIssues)
    extends AccessibilityLinter.Service(AccessibilityLinter.axe, knownIssues) {

  override protected def findViolations(html: String): List[AccessibilityViolation] =
    Json.parse(axeProcess(html)).as[List[AxeAlert]].flatMap(asViolations)

  private def asViolations(axeAlert: AxeAlert): List[AccessibilityViolation] =
    axeAlert.nodes.map { node =>
      AccessibilityViolation(
        linter,
        code = axeAlert.code,
        severity = node.severity,
        alertLevel = alertMapping(node.severity),
        description = node.failureSummary,
        snippet = node.snippet,
        helpUrl = axeAlert.helpUrl,
        knownIssue = false,
        furtherInformation = None
      )
    }

  private val alertMapping: Map[String, String] = Map(
    "info"     -> "INFO",
    "warning"  -> "WARNING",
    "minor"    -> "ERROR",
    "moderate" -> "ERROR",
    "serious"  -> "ERROR",
    "critical" -> "ERROR",
    "error"    -> "ERROR"
  )

  private case class Node(severity: String, alertLevel: String, snippet: String, failureSummary: String)
  private implicit val nodeReads: Reads[Node] = (
    ((JsPath \ "impact").read[String] or Reads.pure("UNDEFINED")) and
      ((JsPath \ "impact").read[String] or Reads.pure("UNDEFINED")) and
      ((JsPath \ "html").read[String] or Reads.pure("UNDEFINED")) and
      ((JsPath \ "failureSummary").read[String] or Reads.pure("UNDEFINED"))
  )(Node.apply _)

  private case class AxeAlert(description: String, helpUrl: String, code: String, nodes: List[Node])
  private implicit val axeAlertReads: Reads[AxeAlert] = (
    ((JsPath \ "description").read[String] or Reads.pure("UNDEFINED")) and
      ((JsPath \ "helpUrl").read[String] or Reads.pure("UNDEFINED")) and
      ((JsPath \ "id").read[String] or Reads.pure("UNDEFINED")) and
      (JsPath \ "nodes").read[List[Node]]
  )(AxeAlert.apply _)
}

object AxeLinter {
  def fromLocalNpmWrapper(workingDir: String, knownIssues: KnownIssues): AxeLinter =
    new AxeLinter(
      html => (Process("node axe", new File(workingDir)) #< new ByteArrayInputStream(html.getBytes("UTF-8"))).!!,
      knownIssues
    )
}
