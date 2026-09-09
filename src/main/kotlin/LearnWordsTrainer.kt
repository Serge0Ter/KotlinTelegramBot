package org.example

import java.io.File

data class Statistics(val totalCount: Int, val learnedCount: Int, val percent: Int)

data class Question(val variants: List<Word>, val correctAnswer: Word)

class LearnWordsTrainer {
    private val words = File("words.txt")
    val dictionary = loadDictionary()

    fun getStatistics(): Statistics {
        val totalCount = dictionary.size
        val learnedCount = dictionary.filter { it.correctAnswersCount >= 3 }.size
        val percent = (learnedCount * 100 / totalCount)
        return Statistics(totalCount, learnedCount, percent)
    }

    fun getNextQuestions(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswersCount < 3 }
        if (notLearnedList.isEmpty()) return null
        val questionWords = notLearnedList.shuffled().take(4)
        val correctAnswer = questionWords.random()
        return Question(
            variants = questionWords,
            correctAnswer = correctAnswer
        )
    }

    fun learnWords(dictionary: List<Word>) {
        while (true) {
            val question = getNextQuestions()
            if (question == null) {
                println("Все слова в словаре выучены")
                return
            }
            val randomTranslate = question.variants.map { it.translate }.shuffled()
            println("\n${question.correctAnswer.original}:")
            randomTranslate.forEachIndexed { i, str -> println(" ${i + 1} - $str") }
            println("----------\n 0 - Меню")
            val answer = readln().toIntOrNull() ?: -1
            val correctAnswerId = randomTranslate.indexOf(question.correctAnswer.translate)
            when (answer) {
                0 -> {
                    println("Выход")
                    return
                }

                in 1..randomTranslate.size -> {
                    if (answer - 1 == correctAnswerId) {
                        question.correctAnswer.correctAnswersCount++
                        saveDictionary(dictionary)
                        println("Правильно!")
                    } else println("Неправильно! ${question.correctAnswer.original} – это ${question.correctAnswer.translate}")
                }

                else -> println("Введите число от 1 до ${randomTranslate.size}")

            }
        }
    }


    private fun loadDictionary(): List<Word> {
        val dictionary = mutableListOf<Word>()
        for (word in words.readLines()) {
            val newWord = word.split("|")
            dictionary.add(Word(newWord[0], newWord[1], newWord.getOrNull(2)?.toIntOrNull() ?: 0))
        }
        return dictionary.toList()
    }

    private fun saveDictionary(dictionary: List<Word>) {
        words.writeText(dictionary.joinToString("\n") { "${it.original}|${it.translate}|${it.correctAnswersCount}" })
    }
}