val commonSettings = Seq(
  organization := "com.lightbend",
  version := "0.0.1",
  crossScalaVersions := Seq("2.12.6", "2.13.0-M4"),
  scalaVersion := crossScalaVersions.value.head,
  crossVersion := CrossVersion.patch,
  scalacOptions ++= Seq(
    "-encoding", "utf-8",
    "-deprecation",
    "-unchecked",
    "-feature",
    "-Xlint",
    "-Xfatal-warnings",
    "-Ywarn-value-discard"
  ))

lazy val plugin =
  project.in(file("plugin"))
    .settings(commonSettings)
    .settings(
      name := "cloc-plugin",
      libraryDependencies +=
        "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided"
    )

lazy val tests =
  project.in(file("tests"))
    .settings(commonSettings)
    .settings(
      scalacOptions ++= Seq(
        s"-Xplugin:${(plugin / Compile / packageBin).value}",
        s"-Xplugin-require:cloc"
      ))
    .dependsOn(plugin)
