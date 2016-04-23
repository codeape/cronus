parallelExecution in ThisBuild := false


lazy val commonSettings = Seq(
  organization := "org.codeape",
  version := "0.0.1",
  scalaVersion := "2.11.7"
  , parallelExecution in ThisBuild := false
  , fork in Test := true
  //parallelExecution in Global := false
  //testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-P1"),
  //fork in Test := true,
  //parallelExecution in Test := false
  //parallelExecution in IntegrationTest := false,
  //concurrentRestrictions in Global += Tags.exclusive(Tags.Test),
  //concurrentRestrictions in Global += Tags.limit(Tags.Test, 1)
  //testOptions in Test += Tests.Argument("sequential")
)

lazy val versions = new {
  val finatra = "2.1.5"
  val guice = "4.0"
  val logback = "1.1.3"
  val webjars_locator = "0.30"
  val mocito_core = "1.9.5"
  val bootstrap = "3.3.6"
  val scalatest = "2.2.3"
  val scalactic = "2.2.3"
  val specs2 = "2.3.12"
  val akka = "2.4.3"
}

lazy val common = (project in file("common")).
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

      "org.mockito" % "mockito-core" % versions.mocito_core % "test",
      "org.scalactic" %% "scalactic" % versions.scalactic,
      "org.scalatest" %% "scalatest" % versions.scalatest % "test",
      "org.specs2" %% "specs2" % versions.specs2 % "test",


      "com.twitter" % "bijection-util_2.11" % "0.9.2",
      "com.typesafe.akka" %% "akka-actor" % versions.akka,
      "com.typesafe.akka" %% "akka-slf4j" % versions.akka,

      "org.webjars" % "webjars-locator" % versions.webjars_locator
    )
  )

lazy val front = (project in file("front")).
  settings(commonSettings: _*).
  settings(
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      "Twitter Maven" at "https://maven.twttr.com"
    ),
    libraryDependencies ++= Seq(
      "org.webjars" % "bootstrap" % versions.bootstrap
    )
  )
  .dependsOn(common % "test->test;compile->compile")
