val appName = "scalatest-accessibility-linter"

val scala2_12 = "2.12.13"
val scala2_13 = "2.13.8"

lazy val root: Project = (project in file("."))
  .configs(IntegrationTest)
  .enablePlugins(SbtTwirl)
  .settings(
    TwirlKeys.templateImports := templateImports,
    TwirlKeys.constructorAnnotations += "@javax.inject.Inject()",
    Compile / TwirlKeys.compileTemplates / sourceDirectories := (Compile / unmanagedResourceDirectories).value,
    Defaults.itSettings,
    scalaVersion := scala2_12,
    crossScalaVersions := Seq(scala2_12, scala2_13),
    isPublicArtefact := true,
    majorVersion := 0,
    name := appName,
    libraryDependencies ++= LibDependencies.all,
    assumedEvictionErrorLevel := Level.Error,
    libraryDependencySchemes += "org.scala-lang.modules" %% "scala-java8-compat" % VersionScheme.Always
  )

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
