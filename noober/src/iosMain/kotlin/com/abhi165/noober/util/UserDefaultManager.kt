package com.abhi165.noober.util

import com.abhi165.noober.NoobRepository
import com.abhi165.noober.SharedPrefManager
import com.abhi165.noober.model.SharedPrefModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import platform.Foundation.NSString
import platform.Foundation.NSUserDefaults
import platform.darwin.NSInteger

internal object UserDefaultManager: SharedPrefManager {
    private val userDefaults = NSUserDefaults.standardUserDefaults
    private val noobUserDefault = NSUserDefaults(suiteName = "noob")

    private val _prefStateFlow = MutableStateFlow(SharedPrefModel())

    override fun getPrefValues(prefName: String): Flow<SharedPrefModel>  {
        getLatestData()
        return  _prefStateFlow
    }

    override fun changeValueOf(key: String, newValue: String, prefName: String, oldValue: Any) {
        setNewValueWithSameType(newValue, oldValue, key)
        getLatestData()
    }

    private fun getLatestData() {
        GlobalScope.launch {
            val data = getAllValuesWithPrefName().first()
            _prefStateFlow.emit(data)
        }
    }

    override suspend fun getAllValuesWithPrefName(): List<SharedPrefModel> {
        val userDefaultData =  userDefaults.dictionaryRepresentation()
        val mappedData = userDefaultData.mapKeys { it.key.toString() }.mapValues { it.value.toString() }
        return listOf(SharedPrefModel("", mappedData))
    }

    fun getValueFor(key: String) = userDefaults.objectForKey(key)

    private fun setNewValueWithSameType(newValue: String, oldValue: Any?, key: String) {
        when (oldValue) {
            is NSInteger -> userDefaults.setInteger(newValue.toLong(), key)
            is NSString -> userDefaults.setObject(newValue, key)
            is Boolean -> userDefaults.setBool(newValue.toBoolean(), key)
            is Float -> userDefaults.setFloat(newValue.toFloat(), key)
            is Double -> userDefaults.setDouble(newValue.toDouble(), key)
        }
        userDefaults.synchronize()
    }

    fun importAccount(prop: Map<String, String>) {
        NSUserDefaults.standardUserDefaults.removeSuiteNamed("noob")
        val isFromAndroid  = prop[Constants.IS_FROM_ANDROID] == "1"
        val deppLinkParameters = prop.toMutableMap().also {
            it.remove(Constants.IS_FROM_ANDROID)
        }

        val userProperties = NoobRepository.userProperties.associate { it.alternateKeyForCrossPlatform to it.key }
        for((key, value ) in deppLinkParameters) {
            val mappedKey = if(isFromAndroid) userProperties[key] else key
            val oldValue = userDefaults.objectForKey(mappedKey ?: key)
            setNewValueWithSameType(
                newValue = value,
                oldValue = oldValue ?: "",
                key = mappedKey ?: key
            )
            noobUserDefault.setObject(oldValue, mappedKey ?: key)
        }
    }

    fun restoreAccount() {
        val values = noobUserDefault.dictionaryRepresentation()
        for((key, oldValue) in values) {
            noobUserDefault.removeObjectForKey(key.toString())
            setNewValueWithSameType(
                newValue = oldValue.toString(),
                oldValue =  userDefaults.objectForKey(key.toString()),
                key = key.toString()
            )
        }
    }
}