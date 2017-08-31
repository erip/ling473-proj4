package edu.washington.rippeth.ling473.proj4

import org.scalatest.FlatSpec

import scala.io.Source

class FileProcessorTest extends FlatSpec {

  private def getResource(resourceName: String) = getClass.getResource(resourceName).getPath

  private val noMatches = getResource("/tests/no_matches/")
  private val singleMatch = getResource("/tests/single_match/")
  private val twoMatches = getResource("/tests/two_matches/")

  private val targetsSuffix = "targets/targets"

  "A FileProcessor" should "find no matches" in {
    val trie = TrieReader.fromFile(s"$noMatches/$targetsSuffix")
    val fileProcessor = new FileProcessor(s"$noMatches/empty.txt", trie)

    assert(fileProcessor.findMatches.isEmpty)
  }

  "A FileProcessor" should "find a single match" in {
    val trie = TrieReader.fromFile(s"$singleMatch/$targetsSuffix")

    val fileProcessor = new FileProcessor(s"$singleMatch/single_match.txt", trie)

    val matches = fileProcessor.findMatches

    assert(matches.nonEmpty)
    assert(matches.size == 1)
  }

  "A FileProcessor" should "find two matches" in {
    val trie = TrieReader.fromFile(s"$twoMatches/$targetsSuffix")

    val fileProcessor = new FileProcessor(s"$twoMatches/back_to_back.txt", trie)

    val matches = fileProcessor.findMatches
    assert(matches.nonEmpty)
    assert(matches.size == 2)
  }
}
