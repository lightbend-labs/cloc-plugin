package com.lightbend.tools

import scala.tools.nsc, nsc.plugins._
import java.io.File

abstract class ClocComponent extends PluginComponent {

  override def description = "count lines of Scala code compiled"

  // the following two members override abstract members in Transform
  val phaseName: String = "cloc"
  def newPhase(prev: nsc.Phase): StdPhase = new ClocPhase(prev)

  class ClocPhase(prev: nsc.Phase) extends StdPhase(prev) {
    override def apply(unit: global.CompilationUnit): Unit = {
      val file = unit.source.file.file
      println(s"$file: ${countLines(file)}")
    }
    override def run() {
      super.run()
      // TODO something at the end?
    }
  }

  private def countLines(file: File): Int = {
    val source = io.Source.fromFile(file)
    try source.getLines.size
    finally source.close()
  }

}
