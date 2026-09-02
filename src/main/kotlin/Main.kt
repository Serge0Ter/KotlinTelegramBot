package org.example

import java.io.File

private val dictionary = mutableListOf<Word>()

fun main() {

    val words: File = File("words.txt")

    try {
        for (word in words.readLines()) {
            val newWord = word.split("|")
            dictionary.add(Word(newWord[0], newWord[1], newWord[3].toInt()))
        }
    } catch (e: Exception) {
        println(e.message)
    }


}