# Project 4

## Task

Given a list of target nucleotide sequences and an entire genome split into multiple files, find the  matches for the sequences within the file, including the file the match was found in, the target, and the hex offset.

## Results

Matches were found.

## Approach

I created trie class which stores the current lowercase character as a byte, a flag of whether or not the node is a target, and
the prefix (i.e., the characters of all ancestor nodes). A node also has up to four children, which were
stored in an array rather than a map for speed and memory efficiency.

I originally did not store the prefix and computed it as I processed the file, but found the runtime of the algorithm
to be intractable, even with O(1) string append operations. Thus, I chose to store the prefix in the trie.

## Tools

For this project, I used primarily the Scala standard library with the exception of a few helper libraries:

- Scalatest, a unit testing library for Scala.
- scala-logging (and Logback), a framework for logging in Scala.

## Testing

Several simple examples were developed into use cases and converted to unit tests. These can be
found 

## To do

While the speed of the processing tends to be "fine", on the order of 20 minutes to complete the
entire genome on my 2.9GHz i7 Mac, the speed could drastically be reduced by leveraging memory-mapped
file IO. Given more time, I would implement this and cut processing time because of the relatively small
size of the DNA files. 
