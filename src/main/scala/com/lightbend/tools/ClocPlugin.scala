package com.lightbend.tools

import scala.tools.nsc, nsc.Global,
  nsc.plugins.{Plugin, PluginComponent},
  java.io.File

class ClocPlugin(val global: Global) extends Plugin {
  override val name = "cloc"
  override val description = "count lines of Scala code compiled"
  override def init(options: List[String], error: String => Unit) = {
    require(options.isEmpty)
    true
  }
  override val components = List(ClocComponent)
  object ClocComponent extends PluginComponent {
    override val global = ClocPlugin.this.global
    override val runsAfter = List("parser")
    override val runsRightAfter = Some("parser")
    override val phaseName: String = "cloc"
    override def newPhase(prev: nsc.Phase): StdPhase = new ClocPhase(prev)
    class ClocPhase(prev: nsc.Phase) extends StdPhase(prev) {
      private val files = collection.mutable.Buffer.empty[File]
      override def apply(unit: global.CompilationUnit): Unit =
        files += unit.source.file.file
      override def run() = {
        super.run()
        import ClocRunner.countLines
        if (!global.settings.isScaladoc)
          println(s"** COMMUNITY BUILD LINE COUNT: ${countLines(files)}")
        ()
      }
    }
  }
}

object ClocRunner {
  // careful, only use flags supported by cloc 1.60 (the version
  // `sudo apt-get install cloc` on the Jenkins workers gave us)
  val command = Seq("cloc", "--progress-rate=0", "--quiet", "--csv")
  def countLines(files: Iterable[File]): Int =
    sys.process.Process(command ++ files.map(_.toString))
      .lineStream.toList  // run to completion, don't leave the process around
      .map(_.split(','))
      .collectFirst{case Array(_, "Scala", _, _, n) => n.toInt}
      .getOrElse(0)
}
