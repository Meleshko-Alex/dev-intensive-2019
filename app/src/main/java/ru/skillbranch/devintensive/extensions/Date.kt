package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time
    //if (value > 0) time += 999

    time += when(units){
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val lastVisit = this.time - date.time
    when(lastVisit) {
        in -360 * DAY .. -26 * HOUR -> return "${getTimeString(lastVisit, TimeUnits.DAY)} назад"
        in -26 * HOUR .. -22 * HOUR -> return "день назад"
        in -22 * HOUR .. -75 * MINUTE -> return "${getTimeString(lastVisit, TimeUnits.HOUR)} назад"
        in -75 * MINUTE .. -45 * MINUTE -> return "час назад"
        in -45 * MINUTE .. -75 * SECOND -> return "${getTimeString(lastVisit, TimeUnits.MINUTE)} назад"
        in -75 * SECOND .. -45 * SECOND -> return "минуту назад"
        in -45 * SECOND .. -2 * SECOND -> return "несколько секунд назад"
        in -1 * SECOND .. 0 -> return "только что"

        in 0 .. 1 * SECOND -> return "сейчас"
        in 2 * SECOND .. 45 * SECOND -> return "через несколько секунд"
        in 45 * SECOND .. 75 * SECOND -> return "через минуту"
        in 75 * SECOND .. 45 * MINUTE -> return "через ${getTimeString(lastVisit, TimeUnits.MINUTE)}"
        in 45 * MINUTE .. 75 * MINUTE -> return "через час"
        in 75 * MINUTE .. 22 * HOUR -> return "через ${getTimeString(lastVisit, TimeUnits.HOUR)}"
        in 22 * HOUR .. 26 * HOUR -> return "через день"
        in 26 * HOUR .. 360 * DAY -> return "через ${getTimeString(lastVisit, TimeUnits.DAY)}"
    }
    return if (lastVisit < 0) "более года назад"
    else "более чем через год"
}

private fun getTimeString(lastVisit: Long, timeUnits: TimeUnits): String {
    val number: Int = getTimeNumber(lastVisit, timeUnits)
    return getTimeWord(number, timeUnits)
}

fun getTimeWord(number: Int, timeUnits: TimeUnits): String {
    var localNumber = number
    val numberString = number.toString()
    val countUnits: CountUnits

    countUnits = if (number in 5..20) CountUnits.OTHER

    else if (number == 1) CountUnits.ONE

    else if (numberString[numberString.length - 1].toString() == "1"
        && numberString.length > 1
        && numberString[numberString.length - 2].toString() != "1") CountUnits.ONE

    else if (numberString[numberString.length - 1].toString() == "2"
        || numberString[numberString.length - 1].toString() == "3"
        || numberString[numberString.length - 1].toString() == "4"
    ) CountUnits.FEW

    else CountUnits.OTHER

    if (localNumber < 0) localNumber *= -1

    when (timeUnits) {
        TimeUnits.MINUTE -> {
            return when (countUnits) {
                CountUnits.ONE -> "$localNumber минуту"
                CountUnits.FEW -> "$localNumber минуты"
                CountUnits.OTHER -> "$localNumber минут"
            }
        }
        TimeUnits.HOUR -> {
            return when (countUnits) {
                CountUnits.ONE -> "$localNumber час"
                CountUnits.FEW -> "$localNumber часа"
                CountUnits.OTHER -> "$localNumber часов"
            }
        }
        TimeUnits.DAY -> {
            return when (countUnits) {
                CountUnits.ONE -> "$localNumber день"
                CountUnits.FEW -> "$localNumber дня"
                CountUnits.OTHER -> "$localNumber дней"
            }
        }
        TimeUnits.SECOND -> return ""
    }
}

fun getTimeNumber(lastVisit: Long, timeUnits: TimeUnits): Int {
    return when (timeUnits) {
        TimeUnits.MINUTE -> Math.round((lastVisit / MINUTE).toDouble()).toInt()
        TimeUnits.HOUR -> Math.round((lastVisit / HOUR).toDouble()).toInt()
        TimeUnits.DAY -> Math.round((lastVisit / DAY).toDouble()).toInt()
        else -> 0
    }
}

enum class TimeUnits{
    SECOND,
    MINUTE,
    HOUR,
    DAY
}
enum class CountUnits{
    ONE,
    FEW,
    OTHER
}