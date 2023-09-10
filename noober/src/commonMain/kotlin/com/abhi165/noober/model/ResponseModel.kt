package com.abhi165.noober.model

internal data class ResponseModel(
    val header: Map<String, String> = mapOf(),
    val body: String = "_",
    val date: String = "_",
    val statusCode: Int = 0,
    val timeInterval: String = "_",
)
