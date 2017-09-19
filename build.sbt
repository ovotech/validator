name := "validator"
organization := "com.stuartizon"
version := "1.1.0"
scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.7",
  "joda-time" % "joda-time" % "2.9.7",
  "org.joda" % "joda-convert" % "1.8.1",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.specs2" %% "specs2-core" % "3.8.7" % Test)

licenses += ("MIT", url("https://opensource.org/licenses/MIT"))