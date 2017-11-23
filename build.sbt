organization := "com.lightbend"
name := "cloc-plugin"
scalaVersion := "2.12.4"
libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)
scalacOptions ++= Seq(
  "-encoding",
  "utf-8",
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
// so we can invoke the Scala compiler at runtime without weird problems
fork in Test := true

val pluginPath = taskKey[File]("path to compiler plugin jar")
enablePlugins(BuildInfoPlugin)
buildInfoKeys := Seq[BuildInfoKey](pluginPath)
pluginPath := (artifactPath in packageBin in Compile).value // TODO
buildInfoPackage := "com.lightbend.tools"
