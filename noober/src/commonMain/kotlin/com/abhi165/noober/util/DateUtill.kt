package com.abhi165.noober.util

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

internal object DateUtil {

    fun now(): String = Clock.System.now().toString()


    fun calculateDiff(from: String, to:String): String {
        val responseTime = from.toInstant()
        val requestTime = to.toInstant()
        val difference = (responseTime - requestTime).absoluteValue
        return difference.toString()
    }

    fun formatTime(string: String): String{
        val time = string.toInstant().toLocalDateTime(TimeZone.currentSystemDefault())
        return "${time.hour}:${time.minute}:${time.second}"
    }
}
