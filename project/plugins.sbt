resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2")
resolvers += Resolver.url("HMRC-open-artefacts-ivy2", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(
  Resolver.ivyStylePatterns
)

addSbtPlugin("uk.gov.hmrc"   % "sbt-auto-build" % "3.22.0")
addSbtPlugin("org.scalameta" % "sbt-scalafmt"   % "2.4.0")

sys.env.get("PLAY_VERSION") match {
  case _ =>
    addSbtPlugin(
      "org.playframework.twirl" % "sbt-twirl" % "2.0.4"
    ) // 2.0.5 doesn't import TemplateN (to be restored in 2.0.6)
}
