package org.example

import java.io.File

private val dictionary = mutableListOf<Word>()

fun main() {

    val words: File = File("words.txt")

    try {
        for (word in words.readLines()) {
            val newWord = word.split("|")
            dictionary.add(Word(newWord[0], newWord[1], newWord.getOrNull(2)?.toInt() ?: 0))
        }
        println(dictionary)
    } catch (e: Exception) {
        println(e.message)
    }


}