package com.abhi165.noober

import com.abhi165.noober.util.DeepLinkHandler
import com.abhi165.noober.util.UserDefaultManager

internal object AccountManagerImpl: AccountManager {
    override suspend fun generateDeepLink(): String {
        val userProp = mutableMapOf<String, String>()
        NoobRepository.userProperties.forEach {prop ->
            userProp[prop.key] = UserDefaultManager.getValueFor(prop.key).toString()
        }
        return DeepLinkHandler.generateDeepLink(userProp)
    }

    override fun restoreAccount() {
        UserDefaultManager.restoreAccount()
    }
}