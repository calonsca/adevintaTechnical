# Adevinta test

_In this repository you can find the code for the Adevinta technical test_

## Information about the project 🛠️


Scala version: "2.13.8"

Libraries used:
```
 - commons-io
 - org.scalatest
```

## The Jar


The jar is located directly in the root of the repository, **adevinta-test.jar**.

To launch the jar on Windows(which is the environment I have used) use the following command:
```
java -cp adevinta-test.jar Main PathToTheFiles

Example:
java -cp adevinta-test.jar Main C:\Dev\adevinta2\data
```

## Notes 📋
_On how some things have been implemented_
* What constitutes a word.
  * For me a word is what adds value, so there are certain words such as prepositions that are not taken into account, that is, both in the file and the text that the user enters, some prepositions do not count.
  * You can find that list in the code, in the var *ListOfPrepositions*.
* What constitutes two words being equal (and matching)
  * If there are several words repeated in the files or in the user's entry, they are only counted once and it does not matter whether they are entered as uppercase, lowercase or alternately.
  * For example, if the user enters "Hello hello HeLlo" it is only counted as one word.
* In the data folder, there are 3 example files, corresponding to news published on the [bbc](https://www.bbc.com/) portal.

