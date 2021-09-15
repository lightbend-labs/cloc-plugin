organization := "com.lightbend"
version := "0.0.1"
name := "cloc-plugin"
scalaVersion := crossScalaVersions.value.head
crossScalaVersions := Seq("2.11.12", "2.12.15", "2.13.6")
crossVersion := CrossVersion.patch
libraryDependencies +=
  "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided"
scalacOptions ++= Seq(
  "-encoding", "utf-8",
  "-deprecation",
  "-unchecked",
  "-feature",
  "-Xlint",
  "-Xfatal-warnings",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard"
)
