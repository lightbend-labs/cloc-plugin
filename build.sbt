organization := "com.lightbend"
version := "0.0.1"
name := "cloc-plugin"
scalaVersion := crossScalaVersions.value.head
crossScalaVersions := Seq("2.12.6", "2.13.0-pre-4ed9119")
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
  "-Ywarn-infer-any",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard"
)
