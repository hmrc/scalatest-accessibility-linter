/*
 * Copyright 2015 HM Revenue & Customs
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

import sbt._

object LibDependencies {

  val play28 = play("play-28")
  val play29 = play("play-29")
  val play30 = play("play-30")

  private def play(playSuffix: String) = Seq(
    "org.scalatest"                %% "scalatest"                       % "3.2.17",
    "com.beachape"                 %% "enumeratum"                      % "1.7.0",
    "com.softwaremill.magnolia1_2" %% "magnolia"                        % "1.1.2",
    "org.reflections"               % "reflections"                     % "0.10.2",
    "org.scalacheck"               %% "scalacheck"                      % "1.14.0",
    "org.scalatestplus.play"       %% "scalatestplus-play"              % scalaTestPlusPlayVersion(playSuffix),
    "nu.validator"                  % "validator"                       % "20.7.2",
    "org.scalatestplus"            %% "scalatestplus-mockito"           % "1.0.0-M2"                     % "test",
    "com.softwaremill.diffx"       %% "diffx-scalatest"                 % "0.5.6"                        % "test",
    "com.vladsch.flexmark"          % "flexmark-all"                    % flexmarkAllVersion(playSuffix) % "test",
    "uk.gov.hmrc"                  %% s"play-frontend-hmrc-$playSuffix" % "8.3.0"                        % "test"
  )

  private def scalaTestPlusPlayVersion(playSuffix: String): String =
    playSuffix match {
      case "play-28" => "5.1.0"
      case "play-29" => "6.0.0"
      case "play-30" => "7.0.0"
    }

  private def flexmarkAllVersion(playSuffix: String): String =
    playSuffix match {
      case "play-28" => "0.62.2"
      case "play-29" => "0.64.8"
      case "play-30" => "0.64.8"
    }
}
