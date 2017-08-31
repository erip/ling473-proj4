package edu.washington.rippeth.ling473.proj4

import com.typesafe.scalalogging.LazyLogging

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Try

case class Match(filename: String, offset: Int, target: String) {
  def hexOffset: String = offset.toHexString
}

class FileProcessor(private val filename: String, private val trieRoot: Trie) extends LazyLogging {

  private val charAndIndex: Iterator[(Char, Int)] = 
    Source.fromFile(filename)
      .zipWithIndex
      .map { case (c, i) => (c.toLower, i)}
       // Remove all non ACTG characters
      .filter { case (c, i) => Trie.Σ.contains(c) }

  private def isTarget(t: Trie, prefix: Char): Boolean = {
    t.contains(prefix) && t.withPrefix(prefix).isTarget
  }

  private def isEmpty(t: Option[Trie]): Boolean = {
    t.isEmpty || (t.isDefined && t.get.children.forall(_.isEmpty))
  }

  private def withPrefix(t: Trie, prefix: Char): Option[Trie] = {
    Try(t.withPrefix(prefix)).toOption
  }

  private def processCurrent(matches: ListBuffer[Match], t: Trie,
                             c: Char, i: Int): (ListBuffer[Match], Trie) = {

    val subtrie = withPrefix(t, c)
    (isTarget(t, c), isEmpty(subtrie)) match {
      case (true, true) => {
        val s = t.withPrefix(c).prefix.toString()
        val m = Match(filename, i - s.length + 1, s)
        logger.info(s"Found match: $m")
        matches.append(m)
        (matches, trieRoot)
      }
      case (true, false) => {
        val s = t.withPrefix(c).prefix.toString()
        val m = Match(filename, i - s.length + 1, s)
        logger.info(s"Found match: $m")
        matches.append(m)
        (matches, subtrie.get)
      }
      case (false, true) => {
        logger.trace("Did not find a match, but I do contain the next char")
        (matches, trieRoot)
      }
      case _ => {
        logger.trace("Did not find a match and I do not contain the next char")

        (matches, subtrie.get)
      }
    }
  }

  def findMatches: Seq[Match] = {
    val (matches, t) = charAndIndex.foldLeft(ListBuffer.empty[Match], trieRoot) {
      case ((acc, t), (char, idx)) =>
        logger.trace(s"Processing $char")
        processCurrent(acc, t, char, idx)
    }
    logger.info(s"Finished processing $filename")
    matches
  }
}
