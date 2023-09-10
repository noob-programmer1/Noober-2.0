package com.abhi165.noober.model

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal data class LogModel(
    val tag:String,
    val value: String,
    val isError: Boolean,
    val date: String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
) {
    fun matchesQuery(query: String): Boolean {
        return tag.contains(query, ignoreCase = true) ||
                value.contains(query, ignoreCase = true) ||
                date.contains(query, ignoreCase = true)
    }
}
