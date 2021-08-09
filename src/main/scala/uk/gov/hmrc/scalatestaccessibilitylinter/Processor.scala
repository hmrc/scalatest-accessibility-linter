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

import java.io.{ByteArrayInputStream, File}
import scala.sys.process.{Process, ProcessBuilder, ProcessLogger}

trait Processor {
  def process(processName: String, left: String): ProcessResult
}

case class NpmProcessor(processDirectory: String) extends Processor {

  override def process(processName: String, left: String): ProcessResult = {
    val processOut = new StringBuilder
    val processErr = new StringBuilder

    (runProcess(processName) #< stream(left)) ! ProcessLogger(processOut append _, processErr append _)
    ProcessResult(name = processName, out = processOut.mkString, error = processErr.mkString)
  }

  private def runProcess(command: String): ProcessBuilder = {
    val processCommand = s"node $command"
    Process(processCommand, new File(processDirectory))
  }

  private def stream(content: String) = new ByteArrayInputStream(content.getBytes("UTF-8"))
}

case class ProcessResult(name: String, out: String, error: String)
