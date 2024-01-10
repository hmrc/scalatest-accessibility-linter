import sbt.Keys._

val scala2_12 = "2.12.18"
val scala2_13 = "2.13.12"

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

lazy val twirlTemplateImports = Seq(
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
  "_root_.play.twirl.api.TwirlHelperImports._",
  "_root_.scalatest-accessibility-linter-play-30/src/test/twirl._",
  "scalatest-accessibility-linter-play-30/src/test/twirl._",
  "uk.gov.hmrc.scalatestaccessibilitylinter.views.html._"
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
  Test / resourceDirectory := (module / Test / resourceDirectory).value
)

lazy val scalatestAccessibilityLinterPlay28 =
  Project("scalatest-accessibility-linter-play-28", file("scalatest-accessibility-linter-play-28"))
    .enablePlugins(
      SbtTwirl
    ) // previously used play sbt-plugin and enabled PlayScala and disabled PlayLayout - this was overkill to add templateImports, and added lots of unnecessary dependencies to created binary (incl. Main-Class config in Manifest)
    .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
    .settings(copySources(scalatestAccessibilityLinterPlay30))
    .settings(
      crossScalaVersions := Seq(scala2_12, scala2_13),
      libraryDependencies ++= LibDependencies.play28,
      Compile / TwirlKeys.compileTemplates / sourceDirectories ++=
        (Compile / unmanagedSourceDirectories).value,
      TwirlKeys.templateImports ++= twirlTemplateImports
    )

lazy val scalatestAccessibilityLinterPlay29 =
  Project("scalatest-accessibility-linter-play-29", file("scalatest-accessibility-linter-play-29"))
    .enablePlugins(SbtTwirl)
    .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
    .settings(copySources(scalatestAccessibilityLinterPlay30))
    .settings(
      crossScalaVersions := Seq(scala2_13),
      libraryDependencies ++= LibDependencies.play29,
      Compile / TwirlKeys.compileTemplates / sourceDirectories ++=
        (Compile / unmanagedSourceDirectories).value,
      TwirlKeys.templateImports ++= twirlTemplateImports
    )

lazy val scalatestAccessibilityLinterPlay30 =
  Project("scalatest-accessibility-linter-play-30", file("scalatest-accessibility-linter-play-30"))
    .enablePlugins(SbtTwirl)
    .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
    .settings(
      crossScalaVersions := Seq(scala2_13),
      libraryDependencies ++= LibDependencies.play30,
      Compile / TwirlKeys.compileTemplates / sourceDirectories ++=
        (Compile / unmanagedSourceDirectories).value,
      TwirlKeys.templateImports ++= twirlTemplateImports
    )
