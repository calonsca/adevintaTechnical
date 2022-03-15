ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "adevinta2"
  )

libraryDependencies += "commons-io" % "commons-io" % "2.11.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % Test

assemblyJarName in assembly := "adevinta-test.jar"