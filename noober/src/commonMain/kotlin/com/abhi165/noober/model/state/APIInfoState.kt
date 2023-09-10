package com.abhi165.noober.model.state



internal data class APIInfoState(
    val summary: APISummaryState,
    val requestState: HeaderBodyState,
    val responseState: HeaderBodyState
) {
    fun matchesQuery(query: String): Boolean {
      return summary.matchesQuery(query) ||
              requestState.matchesQuery(query) ||
              responseState.matchesQuery(query)
    }
}
