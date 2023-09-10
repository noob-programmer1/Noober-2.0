package com.abhi165.noober.model.state

import androidx.compose.ui.graphics.Color

internal data class APISummaryState(
    val cURL: String,
    val statusCode: String,
    val statusCodeColor: Color,
    val timeout: String,
    val timeInterval: String,
    val responseTime: String,
    val requestTime: String,
    val url: String,
    val method: String,
    val cache: String,
    val error:String?
) {
    fun matchesQuery(query: String): Boolean {
        return method.contains(query, ignoreCase = true) ||
                url.contains(query, ignoreCase = true)
    }
}
