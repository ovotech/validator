name := "validator"
organization := "com.stuartizon"
version := "1.3.2"
scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "0.9.0",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.specs2" %% "specs2-core" % "3.8.7" % Test)

licenses += ("MIT", url("https://opensource.org/licenses/MIT"))

// Turn off excessive macro compilation logging
incOptions := incOptions.value.withLogRecompileOnMacro(false)