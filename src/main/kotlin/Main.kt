package org.example

import java.io.File

fun main() {

    val words: File = File("words.txt")
    for (word in words.readLines()) {
        println(word)
    }

}