package com.abhi165.noober.model.state

import com.abhi165.noober.model.LogModel
import com.abhi165.noober.model.SharedPrefModel

internal data class SearchState(
    val queryString: String = "",
    val apiCalls: List<APIInfoState> = listOf(),
    val prefData: List<SharedPrefModel> = listOf(),
    val logs: List<LogModel> = listOf()

)
