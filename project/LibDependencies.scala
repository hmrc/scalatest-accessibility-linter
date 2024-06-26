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

  def play(playSuffix: String, scalaBinaryVersion: String) = Seq(
    "org.scalatest"                   %% "scalatest"                       % "3.2.17",
    "com.beachape"                    %% "enumeratum"                      % "1.7.3",
    magnoliaGroup(scalaBinaryVersion) %% "magnolia"                        % "1.1.2",
    "org.reflections"                  % "reflections"                     % "0.10.2",
    "org.scalacheck"                  %% "scalacheck"                      % "1.18.0",
    "org.scalatestplus.play"          %% "scalatestplus-play"              % "7.0.0",
    "nu.validator"                     % "validator"                       % "20.7.2",
    "com.softwaremill.diffx"          %% "diffx-scalatest-should"          % "0.9.0"  % Test,
    "com.vladsch.flexmark"             % "flexmark-all"                    % "0.64.8" % Test,
    "uk.gov.hmrc"                     %% s"play-frontend-hmrc-$playSuffix" % "10.0.0" % Test
  )

  private def magnoliaGroup(scalaBinaryVersion: String): String =
    scalaBinaryVersion match {
      case "2.13" => "com.softwaremill.magnolia1_2"
      case _      => "com.softwaremill.magnolia1_3"
    }
}
