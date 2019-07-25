package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.extensions.isFirstLetterInLowercase

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        return if (question.answers.contains(answer)) {
            question = question.nextQuestion()
            "Отлично - это правильный ответ!\n${question.question}" to status.color
        } else {
            status = status.nextStatus()
            "Это не правильный ответ!\n${question.question}" to status.color
        }
    }

    fun isValidAnswer(answer: String): Boolean {
        return question.validateQuestion(answer)
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 255, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут", listOf("бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
            override fun validateQuestion(question: String): Boolean {
                return !question.isFirstLetterInLowercase()
            }
        },
        PROFESSION("Назови мою професию", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
            override fun validateQuestion(question: String): Boolean {
                return question.isFirstLetterInLowercase()
            }
        },
        MATERIAL("Из чего я сделан?", listOf("бендер", "металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
            override fun validateQuestion(question: String): Boolean {
                return !question.matches(Regex(".*\\d.*"))
            }
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
            override fun validateQuestion(question: String): Boolean {
                return question.matches(Regex("[0-9]+"))
            }
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
            override fun validateQuestion(question: String): Boolean {
                return question.matches(Regex("[0-9]+")) && question.length == 7
            }
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
            override fun validateQuestion(question: String): Boolean {
                return question.isFirstLetterInLowercase()
            }
        };

        abstract fun nextQuestion(): Question
        abstract fun validateQuestion(question: String): Boolean
    }
}