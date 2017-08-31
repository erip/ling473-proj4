package edu.washington.rippeth.ling473.proj4

import java.io.File

import scala.collection.parallel.ForkJoinTaskSupport

class DirectoryProcessor(dir: String, targetFile: String) {

  private class DirectoryReader(dir: String) {
    val files: Seq[File] = new File(dir) match {
      case d if d.exists && d.isDirectory => d.listFiles.filter(_.isFile)
      case _ => Nil
    }
  }

  private val files = new DirectoryReader(dir).files

  private val trie: Trie = TrieReader.fromFile(targetFile)

  def processDirectory: collection.parallel.ParSeq[Match] = {
    val parFiles = files.par
    parFiles.tasksupport = new ForkJoinTaskSupport(
      new scala.concurrent.forkjoin.ForkJoinPool{
        // Create a thread for each core -- this is the
        // most efficiency we can draw before saturating
        // the cache
        Runtime.getRuntime.availableProcessors
      })
    parFiles.map(f => new FileProcessor(f.getPath, trie)).flatMap(_.findMatches)
  }
}
