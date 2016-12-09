parallelExecution in ThisBuild := false

lazy val commonSettings = Seq(
  organization := "org.codeape",
  version := "0.0.1",
  scalaVersion := "2.11.8",
  parallelExecution in ThisBuild := false,
  fork in Test := false
)

lazy val versions = new {
  val finatra = "2.1.6"
  val guice = "4.0"
  val logback = "1.1.3"
  val webjars_locator = "0.30"
  val mocito_core = "1.9.5"
  val bootstrap = "3.3.6"
  val scalatest = "2.2.3"
  val scalactic = "2.2.3"
  val specs2 = "2.3.12"
  val akka = "2.4.3"
  val util_app = "6.34.0"
  val backbonejs = "1.3.2"
  val underscorejs = "1.8.3"
  val jquery = "1.11.1"
  val requirejs = "2.2.0"
}

lazy val cronusw = (project in file("cronusw")).
  settings(commonSettings: _*).
  settings(
    pipelineStages in Assets := Seq(rjs),
    JsEngineKeys.engineType := JsEngineKeys.EngineType.Node,
    //RjsKeys.mainConfig := "build",
    RjsKeys.optimize := "none",
    //JsEngineKeys.parallelism := 1,
    libraryDependencies ++= Seq(
      "org.webjars" % "requirejs" % versions.requirejs,
      "org.webjars" % "bootstrap" % versions.bootstrap,
      "org.webjars" % "backbonejs" % versions.backbonejs,
      "org.webjars" % "underscorejs" % versions.underscorejs,
      "org.webjars" % "jquery" % versions.jquery
    )
  ).
  enablePlugins(SbtWeb)



lazy val common = (project in file("common")).
  settings(commonSettings: _*).
  settings(
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      "Twitter Maven" at "https://maven.twttr.com"
    ),
    libraryDependencies ++= Seq(
      "com.twitter" %% "util-app" % versions.util_app,
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
  .dependsOn(cronusw)

lazy val front = (project in file("front")).
  settings(commonSettings: _*).
  settings(
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      "Twitter Maven" at "https://maven.twttr.com"
    ),
    libraryDependencies ++= Seq(
    )
  )
  .dependsOn(common % "test->test;compile->compile")

