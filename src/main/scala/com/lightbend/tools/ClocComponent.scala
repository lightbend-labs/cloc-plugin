package com.lightbend.tools

import scala.tools.nsc, nsc.plugins._
import java.io.File

abstract class ClocComponent extends PluginComponent {

  override def description = "count lines of Scala code compiled"

  // the following two members override abstract members in Transform
  val phaseName: String = "cloc"
  def newPhase(prev: nsc.Phase): StdPhase = new ClocPhase(prev)

  val files = collection.mutable.Buffer.empty[File]

  class ClocPhase(prev: nsc.Phase) extends StdPhase(prev) {
    override def apply(unit: global.CompilationUnit): Unit =
      files += unit.source.file.file
    override def run() = {
      super.run()
      if (!global.settings.isScaladoc)
        sys.process.Process("cloc" +: "--include-lang=Scala" +: files.map(_.toString)).!
      ()
    }
  }

}
