package org.example

import java.io.File

fun main() {

    val words: File = File("words.txt")

    try {
        for (word in words.readLines()) {
            println(word)
        }
    } catch (e: Exception) {
        println(e.message)
    }


}