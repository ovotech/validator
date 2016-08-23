name := "validator"
organization := "com.ovoenergy"
organizationName := "Ovo Energy"
organizationHomepage := Some(url("http://www.ovoenergy.com"))
version := "1.0.0"
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.1.4",
  "joda-time" % "joda-time" % "2.8.1",
  "org.joda" % "joda-convert" % "1.2",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.specs2" %% "specs2-core" % "2.3.13-scalaz-7.1.0-RC1" % Test)
