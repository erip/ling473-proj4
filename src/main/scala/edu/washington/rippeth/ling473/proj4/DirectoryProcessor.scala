package edu.washington.rippeth.ling473.proj4

import java.io.File

import com.rklaehn.radixtree.RadixTree

import scala.collection.parallel.ForkJoinTaskSupport

class DirectoryProcessor(dir: String, targetFile: String) {

  private class DirectoryReader(dir: String) {
    val files: Seq[File] = new File(dir) match {
      case d if d.exists && d.isDirectory => d.listFiles.filter(_.isFile)
      case _ => Nil
    }
  }

  private val files = new DirectoryReader(dir).files

  private val trie: RadixTree[String, Boolean] = RadixTreeReader.fromFile(targetFile)

  def processDirectory: collection.parallel.ParSeq[Match] = {
    val parFiles = files.par
    parFiles.tasksupport = new ForkJoinTaskSupport(
      new scala.concurrent.forkjoin.ForkJoinPool{
        Runtime.getRuntime.availableProcessors * Thread.activeCount()
      })
    parFiles.map(f => new FileProcessor(f.getPath, trie)).flatMap(_.findMatches)
  }
}
