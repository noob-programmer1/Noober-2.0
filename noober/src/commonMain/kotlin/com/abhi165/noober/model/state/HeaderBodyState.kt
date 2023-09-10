package com.abhi165.noober.model.state


internal data class HeaderBodyState(
    val headers: Map<String, String>,
    val body: String
) {
    fun matchesQuery(query: String): Boolean {
        return body.contains(query, ignoreCase = true) ||
                headers.any {
                    it.key.contains(query, ignoreCase = true) || it.value.contains(query, ignoreCase = true)
                }
    }
}