organization := "com.lightbend"
version := "0.0.1"
name := "cloc-plugin"
scalaVersion := "2.12.4"
libraryDependencies +=
  "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided"
scalacOptions ++= Seq(
  "-encoding", "utf-8",
  "-deprecation",
  "-unchecked",
  "-feature",
  "-Xlint",
  "-Xfatal-warnings",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-infer-any",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard"
)
