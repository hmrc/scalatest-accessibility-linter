import sbt.Keys._
import uk.gov.hmrc.DefaultBuildSettings

val scala2_12 = "2.12.18"
val scala2_13 = "2.13.12"
val scala3    = "3.3.3"

ThisBuild / majorVersion := 1
ThisBuild / isPublicArtefact := true
ThisBuild / scalaVersion := scala2_13
ThisBuild / scalacOptions += "-Wconf:src=views/.*:s"
ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml"          % VersionScheme.Always // required since we're cross building for Play 2.8 which isn't compatible with sbt 1.9
ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-java8-compat" % VersionScheme.Always

lazy val projects: Seq[ProjectReference] =
  sys.env.get("PLAY_VERSION") match {
    case Some("2.8") => Seq(scalatestAccessibilityLinterPlay28)
    case Some("2.9") => Seq(scalatestAccessibilityLinterPlay29)
    case _           => Seq(scalatestAccessibilityLinterPlay30)
  }

lazy val templateImports: Seq[String] = Seq(
  "_root_.play.twirl.api.Html",
  "_root_.play.twirl.api.HtmlFormat",
  "_root_.play.twirl.api.JavaScript",
  "_root_.play.twirl.api.Txt",
  "_root_.play.twirl.api.Xml",
  "play.api.mvc._",
  "play.api.data._",
  "play.api.i18n._",
  "play.api.templates.PlayMagic._",
  "uk.gov.hmrc.hmrcfrontend.views.html.components.implicits._",
  "_root_.play.twirl.api.TwirlFeatureImports._",
  "_root_.play.twirl.api.TwirlHelperImports._"
)

lazy val library = (project in file("."))
  .settings(publish / skip := true)
  .aggregate(
    projects: _*
  )

def copySources(module: Project) = Seq(
  Compile / scalaSource := (module / Compile / scalaSource).value,
  Compile / resourceDirectory := (module / Compile / resourceDirectory).value,
  Test / scalaSource := (module / Test / scalaSource).value,
  Test / resourceDirectory := (module / Test / resourceDirectory).value,
  Test / TwirlKeys.compileTemplates / sourceDirectories += (module / baseDirectory).value / s"src/test/twirl"
)

lazy val scalatestAccessibilityLinterPlay28 =
  Project("scalatest-accessibility-linter-play-28", file("scalatest-accessibility-linter-play-28"))
    .enablePlugins(SbtTwirl)
    .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
    .settings(copySources(scalatestAccessibilityLinterPlay30))
    .settings(
      crossScalaVersions := Seq(/* TODO confirm we can drop Scala 2.12 (and clean up usage): scala2_12, */scala2_13),
      libraryDependencies ++= LibDependencies.play("play-28", scalaBinaryVersion.value),
      TwirlKeys.constructorAnnotations += "@javax.inject.Inject()",
      TwirlKeys.templateImports ++= templateImports
    )

lazy val scalatestAccessibilityLinterPlay29 =
  Project("scalatest-accessibility-linter-play-29", file("scalatest-accessibility-linter-play-29"))
    .enablePlugins(SbtTwirl)
    .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
    .settings(copySources(scalatestAccessibilityLinterPlay30))
    .settings(
      crossScalaVersions := Seq(scala2_13),
      libraryDependencies ++= LibDependencies.play("play-29", scalaBinaryVersion.value),
      TwirlKeys.constructorAnnotations += "@javax.inject.Inject()",
      TwirlKeys.templateImports ++= templateImports
    )

lazy val scalatestAccessibilityLinterPlay30 =
  Project("scalatest-accessibility-linter-play-30", file("scalatest-accessibility-linter-play-30"))
    .enablePlugins(SbtTwirl)
    .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
    .settings(
      crossScalaVersions := Seq(scala2_13, scala3),
      scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
                          case Some((3, _ )) => Seq("-Xmax-inlines", "64")
                          case _             => Seq.empty
                        }),
      libraryDependencies ++= LibDependencies.play("play-30", scalaBinaryVersion.value),
      Test / TwirlKeys.compileTemplates / sourceDirectories += baseDirectory.value / s"src/test/twirl",
      TwirlKeys.constructorAnnotations += "@javax.inject.Inject()",
      TwirlKeys.templateImports ++= templateImports
    )

lazy val it = project
  .settings(publish / skip := true)
  .aggregate(
    sys.env.get("PLAY_VERSION") match {
      case Some("2.8") => itPlay28
      case Some("2.9") => itPlay29
      case _           => itPlay30
    }
  )

lazy val itPlay28 = (project in file("it-play-28"))
  .enablePlugins(SbtTwirl)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(copySources(itPlay30))
  .settings(DefaultBuildSettings.itSettings())
  .dependsOn(scalatestAccessibilityLinterPlay28 % "test->test")

lazy val itPlay29 = (project in file("it-play-29"))
  .enablePlugins(SbtTwirl)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(copySources(itPlay30))
  .settings(DefaultBuildSettings.itSettings())
  .dependsOn(scalatestAccessibilityLinterPlay29 % "test->test")

lazy val itPlay30 = (project in file("it-play-30"))
  .enablePlugins(SbtTwirl)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(DefaultBuildSettings.itSettings())
  .dependsOn(scalatestAccessibilityLinterPlay30 % "test->test")
