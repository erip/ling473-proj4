package edu.washington.rippeth.ling473.proj4

class Trie(val char: Option[Byte]=None, val isTarget: Boolean = false, val prefix: StringBuilder = StringBuilder.newBuilder) {
  var children: Array[Option[Trie]] = Array.fill(Trie.`|Σ|`)(None)

  def contains(c: Char): Boolean = children(c2i(c)).isDefined

  def withPrefix(c: Char): Trie =
    children(c2i(c)).getOrElse(throw new IllegalArgumentException(s"Character '$c' does not exist in this trie"))

  def append(target: String): Unit = {
    val s = target.toLowerCase
    if(s.isEmpty)
      throw new IllegalArgumentException("Cannot add an empty string")
    else if(s.length == 1) {
      val head = s.head

      val prefix = StringBuilder.newBuilder
      prefix.append(this.prefix)
      prefix.append(head)

      children(c2i(head)) = Some(new Trie(Some(head.toByte), isTarget = true, prefix = prefix))

    } else {
      val (head, rest) = (s.head, s.tail)
      if(this.contains(head)) {
        val next = this.withPrefix(head)
        next.append(rest)
      }
      else {

        val prefix = StringBuilder.newBuilder
        prefix.append(this.prefix)
        prefix.append(head)

        val next = new Trie(Some(head.toByte), isTarget = false, prefix = prefix)
        next.append(rest)
        children(c2i(head)) = Some(next)
      }
    }
  }

  private def c2i(char: Char) = char.toLower match {
    case 'a' => 0
    case 'c' => 1
    case 'g' => 2
    case 't' => 3
    case other => throw new IllegalArgumentException(s"Character '$other' not in the alphabet")
  }
}

object Trie {
  val Σ: String = "actg"
  val `|Σ|`: Int = Σ.length
  def apply(lines: Iterator[String]): Trie = {
    val root = new Trie
    lines.foreach(root.append)
    root
  }
}
