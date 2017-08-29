package edu.washington.rippeth.ling473.proj4

import java.io.{BufferedWriter, File, FileWriter}

import com.typesafe.scalalogging.LazyLogging

object ProjectFourDriver extends App with LazyLogging {

  protected object FormatterTypes extends Enumeration {
    val Output, ExtraCredit = Value

    def createMap(matches: Seq[Match]): Map[FormatterTypes.Value, Formatter] = Map(
      FormatterTypes.Output -> new OutputFormatter(matches),
      FormatterTypes.ExtraCredit -> new ExtraCreditFormatter(matches)
    )
  }

  private def writeToFile(formatter: Formatter): Unit = {
    val file = new File("extra-credit")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(formatter.toString)
    bw.close()
  }

  val directoryName = "/Users/erip/Code/ling473-proj4"
  val targetName = s"$directoryName/tmp/targets"
  val trie = RadixTreeReader.fromFile(targetName)

  val directoryProcessor = new DirectoryProcessor(s"$directoryName/tmp/hg19-GRCh37", targetName)

  val t = System.currentTimeMillis()
  val matches = directoryProcessor.processDirectory.toList
  logger.info(s"Took ${(System.currentTimeMillis() - t) / 1000 / 60} minutes to finish...")

  val formatters = FormatterTypes.createMap(matches)

  println(formatters(FormatterTypes.Output).toString)

  writeToFile(formatters(FormatterTypes.ExtraCredit))
}