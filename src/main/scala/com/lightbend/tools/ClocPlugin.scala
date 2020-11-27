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
    // just after parser is as early as we can possibly run
    override val runsAfter = List("parser")
    // just specifying runsAfter isn't enough, you have to narrow it down
    // using runsBefore as well.  runsRightAfter exists but isn't much
    // use, since phase assembly will complain if two plugins try to
    // run-right-after the same phase
    override val runsBefore = List("namer")
    override val phaseName: String = "cloc"
    override def newPhase(prev: nsc.Phase): StdPhase = new ClocPhase(prev)
    class ClocPhase(prev: nsc.Phase) extends StdPhase(prev) {
      private val files = collection.mutable.Buffer.empty[File]
      override def apply(unit: global.CompilationUnit): Unit = {
        // careful, file.file might be null, e.g. in REPL
        files ++= Option(unit.source.file.file)
        ()
      }
      override def run() = {
        super.run()
        import ClocRunner.countLines
        if (!global.settings.isScaladoc && files.nonEmpty)
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
    try
      sys.process.Process(command ++ files.map(_.toString))
        .!!
        // JDK 11: work around https://github.com/scala/bug/issues/11125
        // without incurring a deprecation warning. was: .lines
        .linesWithSeparators.map(_.stripLineEnd)
        .map(_.split(','))
        .collectFirst{case Array(_, "Scala", _, _, n) => n.toInt}
        .getOrElse(0)
    catch {
      case e: java.io.IOException =>
        Console.err.println(e.getMessage)
        0
    }
}
