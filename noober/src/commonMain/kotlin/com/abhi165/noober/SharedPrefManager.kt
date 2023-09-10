package com.abhi165.noober

import com.abhi165.noober.model.AvailablePrefModel
import com.abhi165.noober.model.SharedPrefModel
import kotlinx.coroutines.flow.Flow

internal interface SharedPrefManager {
    fun hasPrefEncryptionData(): Boolean{return true}
    fun getAvailablePreferences(): List<AvailablePrefModel> = listOf()
    fun getPrefValues(prefName: String): Flow<SharedPrefModel>
    fun cleanResourcesIfNeeded(){}
    fun changeValueOf(key: String,  newValue: String, prefName: String = "", oldValue: Any)
    suspend fun getAllValuesWithPrefName(): List<SharedPrefModel>
    fun updateNoobPrefValue(data: List<AvailablePrefModel>) {}
}