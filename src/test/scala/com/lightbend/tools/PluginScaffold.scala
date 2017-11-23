package com.lightbend.tools

import scala.tools.nsc.{Settings, Global}
import scala.tools.nsc.io.VirtualDirectory
import scala.reflect.internal.util.BatchSourceFile
import scala.reflect.io.AbstractFile

object PluginScaffold {

  def defaultSettings: Settings = {
    val settings = new Settings
    settings.processArgumentString(
      s"""-usejavacp -Xplugin:${BuildInfo.pluginPath} -Xplugin-require:cloc""")
    settings.outputDirs.setSingleOutput(new VirtualDirectory("(memory)", None))
    settings
  }

  def compile(code: java.io.File,
              showSourceInfo: Boolean = false): Unit = {
    val settings = defaultSettings
    val sources = List(new BatchSourceFile(AbstractFile.getFile(code)))
    val compiler = new Global(settings)
    val run = (new compiler.Run)
    run.compileSources(sources)
  }

}
