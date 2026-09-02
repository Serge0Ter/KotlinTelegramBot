package org.example

import java.io.File


fun main() {
    val dictionary = loadDictionary()

    try {
        while (true) {
            println("Меню: \n1 – Учить слова\n2 – Статистика\n0 – Выход")
            val result = readln()
            when (result) {
                "0" -> return
                "1" -> println("Учить слова")
                "2" -> {
                    val totalCount = dictionary.size
                    val learnedCount = dictionary.filter { it.correctAnswersCount >= 3 }.size
                    if (totalCount > 0) {
                        val percent = (learnedCount * 100 / totalCount)
                        println("Выучено $learnedCount из $totalCount | $percent %\n")
                    }
                }

                else -> println("Введите число 1, 2 или 0")
            }
        }
    } catch (e: Exception) {
        println(e.message)
    }
}

fun loadDictionary(): List<Word> {
    val dictionary = mutableListOf<Word>()
    val words = File("words.txt")
    for (word in words.readLines()) {
        val newWord = word.split("|")
        dictionary.add(Word(newWord[0], newWord[1], newWord.getOrNull(2)?.toIntOrNull() ?: 0))
    }
    return dictionary.toList()
}