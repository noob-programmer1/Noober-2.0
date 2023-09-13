package com.abhi165.noober

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.abhi165.noober.model.AvailablePrefModel
import com.abhi165.noober.model.SharedPrefModel
import com.abhi165.noober.util.Constants
import com.abhi165.noober.util.Constants.NOOB_PLACEHOLDER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

internal class NoobPrefManager(private val context: Context) : SharedPrefManager {
    private val NOOB_PREF = "noob_pref"
    private val availableSharedPref = mutableListOf<String>()
    private var activePref: SharedPreferences? = null
    private var activePrefIdentifier = ""
    private val _prefFlow = MutableStateFlow(SharedPrefModel())

    private val listener = OnSharedPreferenceChangeListener { sharedPreferences, _ ->
        val updatedValues = sharedPreferences.all.filterValues { it != null } as Map<String, Any>
        _prefFlow.tryEmit(
            SharedPrefModel(
                prefName = activePrefIdentifier,
                data = updatedValues
            )
        )
    }

    init {
        getAllSharedPref()
    }

    private val noobSharedPref by lazy {
        context.getSharedPreferences(NOOB_PREF, Context.MODE_PRIVATE)
    }

    override fun hasPrefEncryptionData(): Boolean {
        val prefData = noobSharedPref.all
        availableSharedPref.forEach { identifier ->
            if (prefData[identifier] !is Boolean) return false
        }
        return true
    }

    override fun getPrefValues(prefName: String): Flow<SharedPrefModel> {
        activePrefIdentifier = prefName
        val sharedPreference = (if (noobSharedPref.getBoolean(
                prefName,
                false
            )
        ) getEncryptedSharedPref(prefName) else getSharedPref(prefName)).also {
            it.registerOnSharedPreferenceChangeListener(listener)
            activePref = it
        }
        val prefData = sharedPreference.all.filterValues { it != null } as Map<String, Any>
        _prefFlow.value = SharedPrefModel(
            prefName = prefName,
            data = prefData
        )
        return _prefFlow
    }

    override fun cleanResourcesIfNeeded() {
        activePref?.unregisterOnSharedPreferenceChangeListener(listener)
        activePref = null
        activePrefIdentifier = ""
    }

    override suspend fun getAllValuesWithPrefName(): List<SharedPrefModel> {
        val sharedPrefList = mutableListOf<SharedPrefModel>()
        availableSharedPref.forEach { prefName ->
            val isEncrypted = noobSharedPref.getBoolean(prefName, false)
            val sharedPreference =
                if (isEncrypted) getEncryptedSharedPref(prefName) else getSharedPref(prefName)
            val prefData = sharedPreference.all.filterValues { it != null } as Map<String, Any>
            sharedPrefList.add(
                SharedPrefModel(
                    prefName = prefName,
                    data = prefData
                )
            )
        }
        return sharedPrefList
    }


    override fun changeValueOf(key: String, newValue: String, prefName: String, oldValue: Any) {
        val sharedPreference = if (noobSharedPref.getBoolean(
                prefName,
                false
            )
        ) getEncryptedSharedPref(prefName) else getSharedPref(prefName)
        sharedPreference.edit().apply {
            when (oldValue) {
                is String -> putString(key, newValue)
                is Boolean -> putBoolean(key, newValue.toBoolean())
                is Int -> putInt(key, newValue.toInt())
                is Float -> putFloat(key, newValue.toFloat())
                is Long -> putLong(key, newValue.toLong())
            }
        }.apply()
    }

    private fun changeAndBackupValueOf(
        key: String,
        newValue: String,
        prefName: String,
        oldValue: Any
    ) {
        noobSharedPref.edit().apply {
            val noobKey = key + NOOB_PLACEHOLDER + prefName
            putString(noobKey, oldValue.toString())
        }.apply()
        changeValueOf(key, newValue, prefName, oldValue)
    }

    fun importAccount(queryParameter: Map<String, String>) {
        if (!hasPrefEncryptionData()) return
        val oldValues = noobSharedPref.all.filterKeys { it.contains(NOOB_PLACEHOLDER) }
        noobSharedPref.edit(true) {
            oldValues.forEach { (key, _) ->
                remove(key)
            }
        }

        GlobalScope.launch {
            val userProperties =
                NoobRepository.userProperties.associate { it.alternateKeyForCrossPlatform to it.key }
            val oldPrefValues = getAllValuesWithPrefName()
            val isFromAndroid = queryParameter[Constants.IS_FROM_ANDROID] == "1"
            val deppLinkParameters = queryParameter.toMutableMap().also {
                it.remove(Constants.IS_FROM_ANDROID)
            }

            for ((key, value) in deppLinkParameters) {
                val mappedKey = if (isFromAndroid) key else userProperties[key]
                oldPrefValues.forEach { prefModel ->
                    for ((oldKey, oldValue) in prefModel.data) {
                        if (oldKey == mappedKey) {
                            withContext(Dispatchers.Main) {
                                changeAndBackupValueOf(
                                    key = oldKey,
                                    newValue = value,
                                    prefName = prefModel.prefName,
                                    oldValue = oldValue
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun restoreValues() {
        val restorationValues = noobSharedPref.all.filterKeys { it.contains(NOOB_PLACEHOLDER) }
        val noobEdit = noobSharedPref.edit()
        GlobalScope.launch {
            val oldPrefs = getAllValuesWithPrefName()
            for ((key, value) in restorationValues) {
                val noobKey = key.split(NOOB_PLACEHOLDER)
                val prefKey = noobKey[0]
                val prefIdentifier = noobKey[1]
                val oldPrefValue = oldPrefs.first { it.prefName == prefIdentifier }.data[prefKey]
                withContext(Dispatchers.Main) {
                    changeValueOf(
                        key = prefKey,
                        newValue = value.toString(),
                        prefName = prefIdentifier,
                        oldValue = oldPrefValue ?: ""
                    )
                    noobEdit.remove(key)
                }
            }
            noobEdit.apply()
        }
    }

    override fun updateNoobPrefValue(data: List<AvailablePrefModel>) {
        noobSharedPref.edit().apply {
            data.forEach {
                putBoolean(it.prefIdentifier, it.isEncrypted)
            }

        }.apply()
    }

    private fun getSharedPref(identifier: String): SharedPreferences {
        return context.getSharedPreferences(identifier, Context.MODE_PRIVATE)
    }

    private fun getEncryptedSharedPref(identifier: String): SharedPreferences {
        val key = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        return EncryptedSharedPreferences.create(
            context,
            identifier,
            key,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun getAllSharedPref() {
        val dir = File(context.applicationInfo.dataDir, "shared_prefs")

        val prefFiles = dir.listFiles { _, name ->
            (name.endsWith(".xml") && !name.contains(NOOB_PREF))
        }?.map { it.nameWithoutExtension }

        prefFiles?.let {
            availableSharedPref += it
        }
    }

    override fun getAvailablePreferences(): List<AvailablePrefModel> {
        val noobPref = noobSharedPref.all
        return availableSharedPref.map {
            AvailablePrefModel(
                prefIdentifier = it,
                isEncrypted = (noobPref[it] as? Boolean) ?: false
            )
        }
    }

}