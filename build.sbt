lazy val commonSettings = Seq(
  organization := "org.codeape",
  version := "0.0.1",
  scalaVersion := "2.11.7"
)

lazy val versions = new {
  val finatra = "2.1.4"
  val logback = "1.0.13"
  val webjars_locator = "0.30"
  val bootstrap = "3.3.6"
}

lazy val common = (project in file("common")).
  settings(commonSettings: _*)

lazy val front = (project in file("front")).
  settings(commonSettings: _*).
  settings(
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      "Twitter Maven" at "https://maven.twttr.com"
    ),
    libraryDependencies ++= Seq(
      "com.twitter.finatra" %% "finatra-http" % versions.finatra,
      "ch.qos.logback" % "logback-classic" % versions.logback,
      "org.webjars" % "webjars-locator" % versions.webjars_locator,
      "org.webjars" % "bootstrap" % versions.bootstrap
    )
  ).
  dependsOn(common)


