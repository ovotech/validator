name := "validator"
organization := "com.stuartizon"
version := "1.2.0"
scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.0.0",
  "joda-time" % "joda-time" % "2.9.7",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.specs2" %% "specs2-core" % "3.8.7" % Test)

bintrayOrganization := Some("ovotech")
licenses += ("MIT", url("https://opensource.org/licenses/MIT"))