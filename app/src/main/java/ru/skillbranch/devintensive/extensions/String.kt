package ru.skillbranch.devintensive.extensions

fun String.truncate(quantity: Int = 16): String {

    var resultStr = when (this.length) {
        in 0..quantity -> this
        else -> removeSpaces(this, quantity)
    }
    return resultStr
}

fun String.stripHtml(): String {
    val resultStr = this.replace(Regex("<.*?>"), "")
    return resultStr.replace(Regex("\\s+"), " ")
}

fun removeSpaces(tempStr: String, quantity: Int): String {
    val str = tempStr.substring(0, quantity) + "..."
    var resultStr: String
    var countBefore = 0
    var countAfter = 0
    for (i in quantity - 1 downTo 0) {
        if (str[i].toString() == " ") countBefore++
        else break
    }

    resultStr = str.substring(0, quantity - countBefore) + str.substring(quantity)

    for (i in quantity + 2 until str.length) {
        if (str[i].toString() == " ") countAfter++
        else {
            countAfter = 0
            break
        }
    }
    if (countAfter == str.length)
        if (countAfter > 0) resultStr =
            resultStr.substring(0, quantity + 2) + resultStr.substring(resultStr.length - countAfter)
    if (isOnlySpacesInString(quantity, tempStr)) resultStr = resultStr.substring(0, quantity - 2)

    return resultStr
}

fun isOnlySpacesInString(quantity: Int, str: String): Boolean {
    for (i in quantity - 1 until str.length) {
        if (str[i].toString() != " ") return false
    }
    return true
}
