package org.example

import java.io.File

fun main() {

    val words: File = File("words.txt")
    words.forEachLine { println(it) }

}