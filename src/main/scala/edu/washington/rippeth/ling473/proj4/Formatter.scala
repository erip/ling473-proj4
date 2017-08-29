package edu.washington.rippeth.ling473.proj4

abstract class Formatter {

  protected def printHelper: Match => String

  protected def formatEntry(k: String, v: Seq[Match]): String =
    s"""$k
       |${v.map(printHelper).mkString("\n")}""".stripMargin

  def toStringHelper(m: Map[String, Seq[Match]]): String =
    m.map{ case (k,v) => formatEntry(k, v) }.mkString("\n")
}

class OutputFormatter(protected val matches: Seq[Match]) extends Formatter {

  override protected def printHelper: (Match) => String = {
    m: Match => s"\t${m.hexOffset}\t${m.target}"
  }

  private def byFileMatches: Map[String, Seq[Match]] = matches.groupBy(_.filename)
  override def toString: String = super.toStringHelper(this.byFileMatches)
}

class ExtraCreditFormatter(protected val matches: Seq[Match]) extends Formatter {

  override protected def printHelper: (Match) => String = {
    m: Match => s"\t${m.hexOffset}\t${m.filename}"
  }

  private def byTargetMatches: Map[String, Seq[Match]] = matches.groupBy(_.target)
  override def toString: String = super.toStringHelper(this.byTargetMatches)
}
