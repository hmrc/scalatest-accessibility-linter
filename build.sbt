val appName = "scalatest-accessibility-linter"

val scala2_12 = "2.12.13"
val scala2_13 = "2.13.7"

lazy val root: Project = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    scalaVersion := scala2_12,
    crossScalaVersions := Seq(scala2_12, scala2_13),
    isPublicArtefact := true,
    majorVersion := 0,
    name := appName,
    libraryDependencies ++= LibDependencies.all
  )
