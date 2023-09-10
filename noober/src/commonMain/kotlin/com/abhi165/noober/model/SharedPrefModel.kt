package com.abhi165.noober.model

internal data class SharedPrefModel(
    val prefName: String = "",
    val data: Map<String, Any> = mapOf(),
) {
    fun matchesQuery(query: String): Boolean {
        return (data.any {prefData ->
            prefData.key.contains(query, ignoreCase = true) || (prefData.value.toString().contains(query, ignoreCase = true))
        })
    }
}
