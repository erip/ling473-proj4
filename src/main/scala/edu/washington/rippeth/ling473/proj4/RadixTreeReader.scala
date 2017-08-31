package edu.washington.rippeth.ling473.proj4

import scala.io.Source

object TrieReader {
  def fromFile(filename: String): Trie =
    TrieReader.fromLines(Source.fromFile(filename).getLines())

  def fromLines(lines: Iterator[String]): Trie = {
    Trie.apply(lines)
  }
}
