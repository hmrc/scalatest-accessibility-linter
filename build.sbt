import sbt.Keys._
import uk.gov.hmrc.DefaultBuildSettings

val scala2_13 = "2.13.12"
val scala3    = "3.3.3"

ThisBuild / majorVersion := 2
ThisBuild / isPublicArtefact := true
ThisBuild / scalaVersion := scala2_13
ThisBuild / scalacOptions += "-Wconf:src=views/.*:s"

// Throughout this build there is scaffolding in place to support multiple versions of Play Framework
// At the time of writing there is only support for Play 3.0 but this maybe extended if future versions of Play Framework
// are released
lazy val projects: Seq[ProjectReference] =
  sys.env.get("PLAY_VERSION") match {
    case _ => Seq(scalatestAccessibilityLinterPlay30)
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

lazy val scalatestAccessibilityLinterPlay30 =
  Project("scalatest-accessibility-linter-play-30", file("scalatest-accessibility-linter-play-30"))
    .enablePlugins(SbtTwirl)
    .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
    .settings(
      crossScalaVersions := Seq(scala2_13, scala3),
      scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((3, _)) => Seq("-Xmax-inlines", "64")
        case _            => Seq.empty
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
      case _ => itPlay30
    }
  )

lazy val itPlay30 = (project in file("it-play-30"))
  .enablePlugins(SbtTwirl)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(DefaultBuildSettings.itSettings())
  .dependsOn(scalatestAccessibilityLinterPlay30 % "test->test")
