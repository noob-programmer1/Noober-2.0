package com.abhi165.noober

internal object AccountManagerImpl : AccountManager {
    override suspend fun generateDeepLink(): String {
        val prop = NoobRepository.userProperties
        val deepLinkPrefValueMap = mutableMapOf<String, String>()
        val prefValues = NoobHelper.prefManager.getAllValuesWithPrefName()

        prefValues.forEach { prefModel ->
            for ((key, value) in prefModel.data) {
                if (prop.any { it.key == key })
                    deepLinkPrefValueMap[key] = value.toString()
            }
        }
        return DeepLinkHandler.generateDeepLink(deepLinkPrefValueMap)
    }

    override fun restoreAccount() {
        NoobHelper.prefManager.restoreValues()
    }
}