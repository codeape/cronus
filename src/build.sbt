lazy val commonSettings = Seq(
  organization := "org.codeape",
  version := "0.0.1",
  scalaVersion := "2.11.7"
)

lazy val versions = new {
  val finatra = "2.1.4"
  val logback = "1.0.13"
}

lazy val front = (project in file("front")).
  settings(commonSettings: _*).
  settings(
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      "Twitter Maven" at "https://maven.twttr.com"
    ),
    libraryDependencies ++= Seq(
      "com.twitter.finatra" %% "finatra-http" % versions.finatra,
      "ch.qos.logback" % "logback-classic" % versions.logback
    )
  )

lazy val common = (project in file("common")).
  settings(commonSettings: _*)

