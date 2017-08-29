package edu.washington.rippeth.ling473.proj4

import com.rklaehn.radixtree.RadixTree

import scala.io.Source

object RadixTreeReader {
  def fromFile(filename: String): RadixTree[String, Boolean] =
    RadixTreeReader.fromLines(Source.fromFile(filename).getLines().toSeq)

  def fromLines(lines: Seq[String]): RadixTree[String, Boolean] = {
    val pairs = lines.map(line => (line.toLowerCase, true))
    RadixTree[String, Boolean](pairs: _*).packed
  }
}