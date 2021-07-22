val appName = "scalatest-accessibility-linter"

lazy val root: Project = (project in file("."))
  .settings(
    scalaVersion := "2.12.13",
    isPublicArtefact := true,
    majorVersion := 0,
    name := appName,
    libraryDependencies ++= LibDependencies.all
  )
