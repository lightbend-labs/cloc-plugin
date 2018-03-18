package com.lightbend.tools

import scala.tools.nsc, nsc.Global,
  nsc.plugins.{Plugin, PluginComponent}

class ClocPlugin(val global: Global) extends Plugin {

  override val name = "cloc"
  override val description = "count lines of Scala code compiled"

  object component extends {
    override val global = ClocPlugin.this.global
  } with ClocComponent {
    override val runsAfter = List("parser")
    override val runsRightAfter = Some("parser")
  }

  override val components = List[PluginComponent](component)

  override def init(options: List[String], error: String => Unit) = {
    require(options.isEmpty)
    true
  }

}
