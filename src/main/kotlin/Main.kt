package org.example

fun main() {
    val trainer = try {
        LearnWordsTrainer()
    } catch (e: Exception) {
        println("Невозможно загрузить словарь")
        return
    }

    try {
        while (true) {
            println("Меню: \n1 – Учить слова\n2 – Статистика\n0 – Выход")
            val result = readln()
            when (result) {
                "0" -> return
                "1" -> {
                    println("Учить слова")
                    trainer.learnWords()
                }

                "2" -> {
                    val statistics = trainer.getStatistics()
                    println("Выучено ${statistics.learnedCount} из ${statistics.totalCount} | ${statistics.percent} %\n")
                }

                else -> println("Введите число 1, 2 или 0")
            }
        }
    } catch (e: Exception) {
        println(e.message)
    }
}