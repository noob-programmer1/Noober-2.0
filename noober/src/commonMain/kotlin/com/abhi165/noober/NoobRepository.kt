package com.abhi165.noober

import com.abhi165.noober.model.APIInfoModel
import com.abhi165.noober.model.AvailablePrefModel
import com.abhi165.noober.model.LogModel
import com.abhi165.noober.model.NoobUserProperties
import com.abhi165.noober.model.SharedPrefModel
import com.abhi165.noober.model.convertToState
import com.abhi165.noober.model.state.APIInfoState
import com.abhi165.noober.model.state.SearchState
import com.abhi165.noober.ui.components.SearchWidgetState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal object NoobRepository {
    private val _requestList = MutableStateFlow<List<APIInfoState>>(listOf())
    val requestList = _requestList.asStateFlow()

    private val _logsList = MutableStateFlow<List<LogModel>>(listOf())
    val logsList = _logsList.asStateFlow()

    private val _searchState = MutableStateFlow(SearchWidgetState.CLOSED)
    val searchState = _searchState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val prefManager = getSharedPrefManager()

    private val _userProperties = mutableListOf<NoobUserProperties>()
    val userProperties: List<NoobUserProperties> = _userProperties

    var OLD_URL = ""
        private set

    var NEW_URL = ""
        private set

    private val cachedPrefData = mutableListOf<SharedPrefModel>()

    val searchedData = searchQuery.combine(requestList) { query, apiCalls ->
        val filteredCalls = apiCalls.filter { it.matchesQuery(query) }
        val filteredPrefData = cachedPrefData.filter { it.matchesQuery(query) }.map {
            val filteredData = it.data.filter { (key, value) ->
                key.contains(query, ignoreCase = true) || value.toString()
                    .contains(query, ignoreCase = true)
            }
            SharedPrefModel(prefName = it.prefName, data = filteredData)
        }
        return@combine SearchState(prefData = filteredPrefData, apiCalls = filteredCalls)
    }.combine(logsList) { searchState, logs ->
        val query = searchState.queryString
        val filteredLogs = logs.filter { it.matchesQuery(query) }
        return@combine searchState.copy(logs = filteredLogs)
    }.flowOn(Dispatchers.IO)

    fun recordAPI(apiCall: APIInfoModel) {
        _requestList.update { apiList ->
            apiList.toMutableList().apply {
                add(0, apiCall.convertToState())
            }
        }
    }

    fun hasPrefEncryptionData() = prefManager.hasPrefEncryptionData()
    fun getAvailablePreferences() = prefManager.getAvailablePreferences()

    fun getPrefData(prefIdentifier: String): Flow<SharedPrefModel> =
        prefManager.getPrefValues(prefIdentifier).flowOn(Dispatchers.IO)

    fun addPrefData(key: String, newValue: String, prefIdentifier: String, oldValue: Any) {
        prefManager.changeValueOf(key, newValue, prefIdentifier, oldValue)
    }

    fun updateNoobPrefValue(data: List<AvailablePrefModel>) {
        prefManager.updateNoobPrefValue(data)
    }

    fun log(key: String, value: Any, isError: Boolean) {
        _logsList.update { logList ->
            logList.toMutableList().apply {
                add(0, LogModel(tag = key, value = value.toString(), isError = isError))
            }
        }
    }

    fun onSearchQueryCHanged(query: String) {
        _searchQuery.value = query
    }

    private fun updateCachedPrefData() {
        cachedPrefData.clear()
        GlobalScope.launch {
            val data = prefManager.getAllValuesWithPrefName()
            withContext(Dispatchers.Main) {
                cachedPrefData.addAll(0, data)
            }
        }
    }

    fun changeSearchWidgetState(state: SearchWidgetState) {
        updateCachedPrefData()
        _searchState.value = state
    }

    fun changeURL(newURL: String, oldURL: String) {
        OLD_URL = oldURL
        NEW_URL = newURL
    }

    fun addUserProperties(prop: List<NoobUserProperties>) {
        _userProperties.clear()
        _userProperties += prop
    }
    fun cleanResourcesIfNeeded() = prefManager.cleanResourcesIfNeeded()
}