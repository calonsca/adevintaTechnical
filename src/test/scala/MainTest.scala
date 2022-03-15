import org.scalatest.FunSuite

import scala.collection.immutable.ListMap
import scala.collection.mutable.ListBuffer


class MainTest extends FunSuite {

  test("Main.validWords") {
    // validate a preposition word.
    assert(Main.validWords("for", new ListBuffer[String]()) == false)
    // validate a non-preposition word.
    assert(Main.validWords("test", new ListBuffer[String]()) == true)
    // validate a non-preposition word but it is already on the list.
    assert(Main.validWords("test", ListBuffer[String]("test")) == false)
  }

  test("Main.showRanking_SmallMap") {
    val map: Map[String, Integer] = Map("file1" -> 5,
      "file2" -> 5, "file3" -> 4, "file4" -> 3, "file5" -> 0)
    // file2 and file1 -> 100%
    // file3 and file4 -> something
    // file5 -> 0% - not shown
    val smallMap = ListMap(map.toSeq.sortWith(_._2 > _._2): _*)
    println("Testing Main.showRanking_SmallMap")
    Main.showRanking(smallMap, 5)
  }

  test("Main.showRanking_BigggerMap"){
    val bigMap: Map[String, Integer] = Map("file1" -> 5,
      "file2" -> 5, "file3" -> 5, "file4" -> 5, "file5" -> 5, "file6" -> 4, "file7" -> 4, "file8" -> 3, "file9" -> 2, "file10" -> 1, "file11" -> 0)
    val BigMapAux = ListMap(bigMap.toSeq.sortWith(_._2 > _._2): _*)
    // 100% -> file1, file2, file3, file4, file5
    // 80% -> file6 and file7 same value
    // 60% -> file8
    // 40% -> file9
    // 20% -> file10
    // file11 -> 0% - not shown
    println("Testing Main.showRanking_BigggerMap")
    Main.showRanking(BigMapAux, 5)
  }


  test("Main.removeFromUserInput"){
    val words = "Hello World of Warcraft"
    var listResult = Main.removeFromUserInput(words)
    assert(listResult.size == 3)
    assert(listResult.head == "hello")
    assert(listResult.last == "warcraft")

    val badWords = "Of about at"
    listResult = Main.removeFromUserInput(badWords)
    assert(listResult.size == 0)
    assert(listResult.isEmpty == true)
  }

}
