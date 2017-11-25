package com.lightbend.tools

import scala.tools.nsc, nsc.plugins._
import java.io.File

object ClocComponent {
  // careful here about only using flags supported by the available cloc version.
  // as of November 2017, on the behemoths `sudo apt-get install cloc` got us
  // version 1.60 which is pretty old
  val command = Seq("cloc", "--progress-rate=0", "--quiet", "--csv")
  def countLines(files: Seq[File]): Int =
    sys.process.Process(command ++ files.map(_.toString))
      .lineStream
      .toList  // run to completion, don't leave the process around
      .map(_.split(','))
      .collectFirst{case Array(_, "Scala", _, _, n) => n.toInt}
      .getOrElse(0)
}

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
      import ClocComponent.countLines
      if (!global.settings.isScaladoc)
        println(s"** COMMUNITY BUILD LINE COUNT: ${countLines(files)}")
      ()
    }
  }

}
