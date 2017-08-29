package edu.washington.rippeth.ling473.proj4

import com.rklaehn.radixtree.RadixTree
import org.scalatest.FlatSpec

class RadixTreeReaderTest extends FlatSpec {

  private def isTerminal(t: RadixTree[String, Boolean], prefix: String) =
    t.getOrDefault(prefix.toLowerCase, false)

  private def withPrefix(t: RadixTree[String, Boolean], prefix: String): RadixTree[String, Boolean] =
    t.subtreeWithPrefix(prefix.toLowerCase)


  "A RadixTree" should "find terminals" in {
    val needle = "Hello"
    val lines = Seq(needle).map(_.toLowerCase)
    val haystack = RadixTreeReader.fromLines(lines)
    // The subtree should be empty
    assert(isTerminal(haystack, needle))
  }

  "A RadixTree" should "not find terminals" in {
    val needle = "Hello"
    val lines = Seq(needle).map(_.toLowerCase)
    val haystack = RadixTreeReader.fromLines(lines)
    // The subtree should be empty
    assert(!isTerminal(haystack, needle.substring(0, needle.length-1)))
  }

  "A RadixTree's subtree" should "still find terminals" in {
    val needle = "Hello"
    val lines = Seq(needle).map(_.toLowerCase)
    val haystack = RadixTreeReader.fromLines(lines)
    // The subtree should be empty
    val (first, rest) = needle.splitAt(1)
    val subtrie = haystack.subtreeWithPrefix(first.toString.toLowerCase)
    assert(isTerminal(subtrie, rest))
  }

  "A RadixTree" should "be empty" in {
    val lines = Nil
    val haystack = RadixTreeReader.fromLines(lines)

    assert(haystack.isEmpty)
  }

  "A RadixTree" should "not be empty" in {
    val needle = "hello"
    val lines = Seq(needle).map(_.toLowerCase)
    val haystack = RadixTreeReader.fromLines(lines)

    assert(!haystack.isEmpty)
  }

  "A RadixTree's subtree" should "be empty" in {
    val needle = "hello"
    val lines = Seq(needle).map(_.toLowerCase)
    val haystack = RadixTreeReader.fromLines(lines)

    assert(withPrefix(haystack, needle.toLowerCase).isEmpty)
  }

  "A RadixTree's subtree" should "not be empty" in {
    val needle = "hello"
    val lines = Seq(needle).map(_.toLowerCase)
    val haystack = RadixTreeReader.fromLines(lines)

    assert(!withPrefix(haystack, needle.toLowerCase.substring(0, needle.length-1)).isEmpty)
  }
}
