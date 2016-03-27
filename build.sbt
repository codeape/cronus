lazy val commonSettings = Seq(
  organization := "org.codeape",
  version := "0.0.1",
  scalaVersion := "2.11.7"
)

lazy val versions = new {
  val finatra = "2.1.5"
  val guice = "4.0"
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
      "com.twitter.finatra" %% "finatra-httpclient" % versions.finatra,
      "ch.qos.logback" % "logback-classic" % versions.logback,

      "com.twitter.finatra" %% "finatra-http" % versions.finatra % "test",
      "com.twitter.finatra" %% "finatra-jackson" % versions.finatra % "test",
      "com.twitter.inject" %% "inject-server" % versions.finatra % "test",
      "com.twitter.inject" %% "inject-app" % versions.finatra % "test",
      "com.twitter.inject" %% "inject-core" % versions.finatra % "test",
      "com.twitter.inject" %% "inject-modules" % versions.finatra % "test",
      "com.google.inject.extensions" % "guice-testlib" % versions.guice % "test",

      "com.twitter.finatra" %% "finatra-http" % versions.finatra % "test" classifier "tests",
      "com.twitter.finatra" %% "finatra-jackson" % versions.finatra % "test" classifier "tests",
      "com.twitter.inject" %% "inject-server" % versions.finatra % "test" classifier "tests",
      "com.twitter.inject" %% "inject-app" % versions.finatra % "test" classifier "tests",
      "com.twitter.inject" %% "inject-core" % versions.finatra % "test" classifier "tests",
      "com.twitter.inject" %% "inject-modules" % versions.finatra % "test" classifier "tests",

      "org.mockito" % "mockito-core" % "1.9.5" % "test",
      "org.scalactic" %% "scalactic" % "2.2.3",
      "org.scalatest" %% "scalatest" % "2.2.3" % "test",
      "org.specs2" %% "specs2" % "2.3.12" % "test",

      "org.webjars" % "webjars-locator" % versions.webjars_locator,
      "org.webjars" % "bootstrap" % versions.bootstrap
    )
  ).
  dependsOn(common)


