package com.abhi165.noober.model

internal data class RequestModel(
    val baseURL: String = "_",
    val curl: String = "_",
    val header: Map<String, String> = mapOf(),
    val body: String = "_",
    val date: String = "_",
    val method: String = "_",
    val timeout: Float = 0f,
    val cachePolicy: String = "_"
)
