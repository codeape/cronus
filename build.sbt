import xerial.sbt.Pack._

parallelExecution in ThisBuild := false

lazy val commonSettings = Seq(
  organization := "org.codeape",
  version := "0.0.1",
  scalaVersion := "2.11.8",
  parallelExecution in ThisBuild := false,
  fork in Test := false
)

lazy val versions = new {
  val finatra = "2.6.0"
  val guice = "4.0"
  val logback = "1.1.3"
  val webjars_locator = "0.30"
  val mocito_core = "1.9.5"
  val bootstrap = "3.3.6"
  val scalatest = "3.0.0"
  val scalactic = "3.0.0"
  val specs2 = "2.3.12"
  val akka = "2.4.3"
  val util_app = "6.39.0"
  val backbonejs = "1.3.2"
  val underscorejs = "1.8.3"
  val jquery = "1.11.1"
  val requirejs = "2.2.0"
  val mustachejs = "2.2.1"
}

lazy val spi = (project in file("spi")).
  settings(commonSettings: _*).
  settings(
    persistLauncher := true,
    persistLauncher in Test := false,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.1"
    )
  ).
  enablePlugins(ScalaJSPlugin, ScalaJSWeb)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    packAutoSettings,
    scalaJSProjects := Seq(spi),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    // triggers scalaJSPipeline when using compile or continuous compilation
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    // Frontend depdendency configuration
    WebKeys.packagePrefix in Assets := "public/",
    managedClasspath in Runtime += (packageBin in Assets).value,

    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      "Twitter Maven" at "https://maven.twttr.com"
    ),
    libraryDependencies ++= Seq(
      "com.twitter" %% "util-app" % versions.util_app,
      "com.twitter" %% "finatra-http" % versions.finatra,
      "com.twitter" %% "finatra-httpclient" % versions.finatra,
      "ch.qos.logback" % "logback-classic" % versions.logback,

      "com.twitter" %% "finatra-http" % versions.finatra % "test",
      "com.twitter" %% "finatra-jackson" % versions.finatra % "test",
      "com.twitter" %% "inject-server" % versions.finatra % "test",
      "com.twitter" %% "inject-app" % versions.finatra % "test",
      "com.twitter" %% "inject-core" % versions.finatra % "test",
      "com.twitter" %% "inject-modules" % versions.finatra % "test",
      "com.google.inject.extensions" % "guice-testlib" % versions.guice % "test",

      "com.twitter" %% "finatra-http" % versions.finatra % "test" classifier "tests",
      "com.twitter" %% "finatra-jackson" % versions.finatra % "test" classifier "tests",
      "com.twitter" %% "inject-server" % versions.finatra % "test" classifier "tests",
      "com.twitter" %% "inject-app" % versions.finatra % "test" classifier "tests",
      "com.twitter" %% "inject-core" % versions.finatra % "test" classifier "tests",
      "com.twitter" %% "inject-modules" % versions.finatra % "test" classifier "tests",

      "org.mockito" % "mockito-core" % versions.mocito_core % "test",
      "org.scalactic" %% "scalactic" % versions.scalactic,
      "org.scalatest" %% "scalatest" % versions.scalatest % "test",
      "org.specs2" %% "specs2" % versions.specs2 % "test",

      "org.webjars.bower" % "bootstrap" % versions.bootstrap % "test",
      "org.webjars.bower" % "backbone" % versions.backbonejs % "test",

      "com.twitter" % "bijection-util_2.11" % "0.9.2",
      "com.typesafe.akka" %% "akka-actor" % versions.akka,
      "com.typesafe.akka" %% "akka-slf4j" % versions.akka,

      "org.webjars" % "webjars-locator" % versions.webjars_locator
    )
  )
  .enablePlugins(SbtWeb)


