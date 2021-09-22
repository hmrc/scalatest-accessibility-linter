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
  val all: Seq[ModuleID] = Seq(
    "com.typesafe.play"      %% "play-json"       % "2.8.1",
    "org.pegdown"             % "pegdown"         % "1.6.0",
    "org.scalatest"          %% "scalatest"       % "3.2.9",
    "com.typesafe"            % "config"          % "1.4.1",
    "com.beachape"           %% "enumeratum"      % "1.7.0",
    "nu.validator"            % "validator"       % "20.7.2",
    "com.typesafe.play"      %% "twirl-api"       % "1.5.1"   % "test",
    "com.vladsch.flexmark"    % "flexmark-all"    % "0.35.10" % "it,test",
    "com.softwaremill.diffx" %% "diffx-scalatest" % "0.5.6"   % "it,test"
  )
}
