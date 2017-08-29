package edu.washington.rippeth.ling473.proj4

import com.rklaehn.radixtree.RadixTree
import com.typesafe.scalalogging.LazyLogging

import scala.io.Source

case class Match(filename: String, offset: Int, target: String) {
  def hexOffset: String = offset.toHexString
}

class FileProcessor(private val filename: String, private val trie: RadixTree[String, Boolean]) extends LazyLogging {

  private val charAndIndex: Iterator[(Char, Int)] = Source.fromFile(filename).zipWithIndex

  private def isTarget(t: RadixTree[String, Boolean], prefix: Char): Boolean = {
    t.getOrDefault(prefix.toString.toLowerCase, false)
  }

  private def isEmpty(t: RadixTree[String, Boolean]): Boolean = {
    val keys = t.keys
    keys.isEmpty || (keys.size == 1 && keys.head.isEmpty)
  }

  private def isEmpty(t: RadixTree[String, Boolean], prefix: Char): Boolean =
    this.isEmpty(t)

  private def withPrefix(t: RadixTree[String, Boolean], prefix: Char): RadixTree[String, Boolean] = {
    t.subtreeWithPrefix(prefix.toString.toLowerCase).packed
  }

  private def processCurrent(matches: Seq[Match], t: RadixTree[String, Boolean], sb: StringBuffer,
                             c: Char, i: Int): (Seq[Match], RadixTree[String, Boolean], StringBuffer) = {
    sb.append(c)
    val s = sb.toString.toLowerCase

    val subtrie = withPrefix(t, c).packed
    (isTarget(t, c), isEmpty(subtrie)) match {
      case (true, true) => {
        val m = Match(filename, i - s.length + 1, s)
        logger.info(s"Found match: $m")
        (matches :+ m, trie, new StringBuffer)
      }
      case (true, false) => {
        val m = Match(filename, i - s.length + 1, s)
        logger.info(s"Found match: $m")
        (matches :+ m, subtrie, sb)
      }
      case (false, true) => {
        (matches, trie, new StringBuffer)
      }
      case _ => {
        (matches, subtrie, sb)
      }
    }
  }

  def findMatches: Seq[Match] = {
    val (matches, _, _) = charAndIndex.foldLeft(Seq.empty[Match], trie, new StringBuffer) {
      case ((acc, t, sb), (char, idx)) =>
        processCurrent(acc, t, sb, char, idx)
    }
    logger.info(s"Finished processing $filename")
    matches
  }
}
