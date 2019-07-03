package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.split(" ")

        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)

        println("The length of String = ${fullName!!.length}")

        //return Pair(firstName, lastName)
        return if (fullName == "" || fullName == " ") null to null
        else firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
        val lettersMap = mapOf("а" to "a", "б" to "b", "в" to "v", "г" to "g", "д" to "d", "е" to "e",
        "ё" to "e", "ж" to "zh", "з" to "z", "и" to "i", "й" to "i", "к" to "k", "л" to "l", "м" to "m", "н" to "n",
        "о" to "o", "п" to "p", "р" to "r", "с" to "s", "т" to "t", "у" to "u", "ф" to "f", "х" to "h", "ц" to "c",
        "ч" to "ch", "ш" to "sh", "щ" to "sh'", "ъ" to "", "ы" to "i", "ь" to "", "э" to "e", "ю" to "yu","я" to "ya")

        val sb = StringBuilder()
        var tempLetter: String
        for (i in 0 until payload.length) {
            tempLetter = when {
                payload[i].toString() == " " -> {
                    divider
                }
                payload[i].toLowerCase().toString() in lettersMap -> {
                    ("${lettersMap[payload[i].toLowerCase().toString()]}")
                }
                else -> {
                    payload[i].toString()
                }
            }
            if (i > 0 && payload[i - 1].toString() == " " || i == 0) {
                if (tempLetter.length == 2) {
                    sb.append(tempLetter[0].toUpperCase())
                    sb.append(tempLetter[1])
                } else {
                    sb.append(tempLetter.toUpperCase())
                }
            }
            else sb.append(tempLetter)
        }
        return sb.toString()
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val firstLetter: String? = firstName?.replace(" ", "", true)?.getOrNull(0)?.toString()?.toUpperCase()
        val secondLetter: String? = lastName?.replace(" ", "", true)?.getOrNull(0)?.toString()?.toUpperCase()

        return if (firstLetter.isNullOrEmpty() && secondLetter.isNullOrEmpty()) null
        else if (firstLetter.isNullOrEmpty()) secondLetter?.get(0).toString()
        else if (secondLetter.isNullOrEmpty()) firstLetter[0].toString()
        else "$firstLetter$secondLetter"
    }
}