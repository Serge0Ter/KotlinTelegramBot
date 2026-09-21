package org.example

import java.io.File

data class Statistics(val totalCount: Int, val learnedCount: Int, val percent: Int)

data class Question(val variants: List<Word>, val correctAnswer: Word)

fun Question.questionToString(): String {
    val variants = this.variants.mapIndexed { index, word ->
        " ${index + 1} - ${word.translate}"
    }.joinToString(separator = "\n")
    return this.correctAnswer.original + "\n" + variants + "\n----------\n 0 - Меню"
}

class LearnWordsTrainer(private val learnedAnswerCount: Int = 3, private val countOfQuestionWords: Int = 4) {
    private val words = File("words.txt")
    private var question: Question? = null
    val dictionary = loadDictionary()

    fun getStatistics(): Statistics {
        val totalCount = dictionary.size
        val learnedCount = dictionary.filter { it.correctAnswersCount >= 3 }.size
        val percent = (learnedCount * 100 / totalCount)
        return Statistics(totalCount, learnedCount, percent)
    }

    fun getNextQuestions(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswersCount < learnedAnswerCount }
        if (notLearnedList.isEmpty()) return null
        val questionWords = if (notLearnedList.size < countOfQuestionWords) {
            val learnedList = dictionary.filter { it.correctAnswersCount >= learnedAnswerCount }.shuffled()
            notLearnedList.shuffled()
                .take(countOfQuestionWords) + learnedList.take(countOfQuestionWords - notLearnedList.size)
        } else {
            notLearnedList.shuffled().take(countOfQuestionWords)
        }.shuffled()
        val correctAnswer = questionWords.random()
        question = Question(
            variants = questionWords,
            correctAnswer = correctAnswer
        )
        return question
    }

    fun learnWords() {
        while (true) {
            val question = getNextQuestions()
            if (question == null) {
                println("Все слова в словаре выучены")
                return
            }
            val randomTranslate = question.variants.map { it.translate }.shuffled()
            println(question.questionToString())
            val correctAnswerInput = readln().toIntOrNull() ?: -1
            when (correctAnswerInput) {
                0 -> {
                    println("Выход")
                    return
                }

                in 1..randomTranslate.size -> {

                    if (checkAnswer(correctAnswerInput.minus(1))) {
                        println("Правильно!")
                    } else println("Неправильно! ${question.correctAnswer.original} – это ${question.correctAnswer.translate}")
                }

                else -> println("Введите число от 1 до ${randomTranslate.size}")

            }
        }
    }

    fun checkAnswer(userAnswerInput: Int?): Boolean {
        return question?.let {
            val correctAnswerId = it.variants.indexOf(it.correctAnswer)
            if (correctAnswerId == userAnswerInput) {
                it.correctAnswer.correctAnswersCount++
                saveDictionary(dictionary)
                true
            } else {
                false
            }
        } ?: false
    }

    private fun loadDictionary(): List<Word> {
        try {
            val dictionary = mutableListOf<Word>()
            for (word in words.readLines()) {
                val newWord = word.split("|")
                dictionary.add(Word(newWord[0], newWord[1], newWord.getOrNull(2)?.toIntOrNull() ?: 0))
            }
            return dictionary.toList()
        } catch (_: IndexOutOfBoundsException) {
            throw IllegalStateException("Некорректный файл")
        }
    }

    private fun saveDictionary(dictionary: List<Word>) {
        words.writeText(dictionary.joinToString("\n") { "${it.original}|${it.translate}|${it.correctAnswersCount}" })
    }
}