import java.io.File
import org.apache.commons.io.FileUtils

import scala.collection.mutable.ListBuffer
import java.nio.charset.StandardCharsets
import scala.util.control.Breaks._
import scala.collection.immutable.ListMap

object Main {

  // List of prepositions to ignore.
  val ListOfPrepositions: List[String] = List("about", "at", "by", "from", "for", "in", "of", "to", "with")

  def main(args: Array[String]): Unit = {
    if (args.isEmpty) {
      throw new IllegalArgumentException("No directory given to index.")
    }

    val indexableDirectory = new File(args.head)

    val mapFiles: Map[String, Vector[String]] = readFiles(indexableDirectory)
    //val mb = 1024*1024
    //println("** Used Memory:  " + (Runtime.getRuntime.totalMemory - Runtime.getRuntime.freeMemory) / mb + " mb.")
    println(indexableDirectory.listFiles().length + " files read in directory " + args.head)

    breakable {
      while (true) {
        val userInput = scala.io.StdIn.readLine("search> ")
        if (userInput equals ":quit") {
          break
        }
        else {
          val listValidWordsUserInput = removeFromUserInput(userInput)
          val mapWithPoints: Map[String, Integer] = searchInTheMap(mapFiles, listValidWordsUserInput)
          createRanking(mapWithPoints, listValidWordsUserInput)
        }
      }
    }

    println("End of the program.")
  }

  /**
   * Iterate through each of the files in the variable indexableDirectory, read all the lines and filter them:
   *  - The lines that have one or more blanks (white spaces) are eliminated.
   *  - Those words that are in the ListOfPrepositions list.
   *
   * The words that pass these two filters are added to a Vector.
   * A map is created whose key is the file name and the value is the vector with the valid words.
   *
   * @param indexableDirectory List of files in the directory provided.
   * @return A map with the key the name of the file and as value the vector of words in the file that have passed the filter.
   */
  def readFiles(indexableDirectory: File): Map[String, Vector[String]] = {
    var listValidWords = new ListBuffer[String]()
    var finalMap: Map[String, Vector[String]] = Map()

    indexableDirectory.listFiles().foreach(file => {
      val totalLines = FileUtils.readLines(file, StandardCharsets.UTF_8)
      totalLines.forEach(line => {
        // avoid read white space in line, one o more whitespace
        val noEmptyLine = line.replaceAll("\\s+", "")
        if (noEmptyLine.nonEmpty) {
          val splitLine = line.toLowerCase.trim.split(" ")
          splitLine.foreach(word => {
            if (validWords(word, listValidWords)) {
              listValidWords += word
            }
          })
        }
      })

      val vectorOfWords = listValidWords.toVector
      val nameOfFile = file.getName
      finalMap += (nameOfFile -> vectorOfWords)
      listValidWords = listValidWords.empty
    })
    finalMap
  }

  /**
   * It iterates through the different files and searches in each Vector that has each file if there is a match between the words entered by the user and the words in the file.
   * @param mapFiles A map with the key the name of the file and as value the vector of words in the file that have passed the filter.
   * @param listValidWordsUserInput List of valid words entered by the user.
   * @return  Map whose key is the file name and value is the number of hits, is not ordered.
   */
  def searchInTheMap(mapFiles: Map[String, Vector[String]], listValidWordsUserInput: List[String]): Map[String, Integer] = {
    var mapWithPoints: Map[String, Integer] = Map()

    for ((file, vectorOfWords: Vector[String]) <- mapFiles) {
      var hits: Integer = 0
      listValidWordsUserInput.foreach(word => {
        vectorOfWords.find(_ == word) match {
          case Some(i) => hits += 1
          case None => None
        }
      })
      mapWithPoints += (file -> hits)
      // reset hits
      hits = 0
    }
    mapWithPoints
  }

  /**
   * The words entered by the user are transformed to lowerCase and the {@link validWords} method is used to check that it is a valid word.
   *
   * @param userInput The string entered by the user.
   * @return A list of valid words.
   */
  def removeFromUserInput(userInput: String): List[String] = {
    val splitLine = userInput.toLowerCase.split(" ")
    val listValidWords = ListBuffer[String]()
    splitLine.foreach(word => {
      if (validWords(word, listValidWords)) {
        listValidWords += word
      }
    })
    listValidWords.toList
  }

  /**
   * Check that the word is not in the list of prepositions and that it is not already in the list of valid words.
   *
   * @param word           The word to evaluate.
   * @param listValidWords List that with have the valid words so far.
   * @return
   */
  def validWords(word: String, listValidWords: ListBuffer[String]): Boolean = {
    if ((!ListOfPrepositions.contains(word)) && (!listValidWords.contains(word))) {
      true
    }
    else {
      false
    }
  }

  /**
   * Create the ranking and call the {@link showRanking} function to print the ranking.
   *
   * @param mapWithPoints           Map whose key is the file name and value is the number of hits, is not ordered.
   * @param listValidWordsUserInput List of all the words entered by the user.
   */
  def createRanking(mapWithPoints: Map[String, Integer], listValidWordsUserInput: List[String]): Unit = {
    // Order the map high to low
    val mapWithPointsOrdered: Map[String, Integer] = ListMap(mapWithPoints.toSeq.sortWith(_._2 > _._2): _*)
    if (mapWithPointsOrdered.size >= 10) {
      showRanking(mapWithPointsOrdered.take(10), listValidWordsUserInput.length)
    }
    else {
      // we have less than 10 files
      showRanking(mapWithPointsOrdered, listValidWordsUserInput.length)
    }
  }

  /**
   * Displays the score according to the words entered.
   *
   * @param mapWithPointsOrdered Map whose key is the file name and value is the number of hits. The map is sorted according to the value from highest to lowest.
   * @param totalWordsUser       The number of words entered by the user.
   */
  def showRanking(mapWithPointsOrdered: Map[String, Integer], totalWordsUser: Integer): Unit = {
    var someHit: Boolean = false
    for ((file, points) <- mapWithPointsOrdered) {

      if (points > 0) {
        if (points == totalWordsUser) {
          //got all the words right
          println(file + " : " + "100%")
          someHit = true
        }
        else {
          //some words
          someHit = true
          //The rating system is a basic rule of three.
          val score = (points * 100) / totalWordsUser
          println(file + " : " + score + "%")
        }
      }
    }
    if (!someHit) {
      println("no matches found")
    }
  }
}
