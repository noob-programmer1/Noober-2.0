package com.abhi165.noober

import android.net.Uri
import com.abhi165.noober.util.Constants

internal object DeepLinkHandler {

    fun generateDeepLink(queries: Map<String, String>): String {
        val uriBuilder = Uri.Builder()
            .scheme(Constants.NOOB_SCHEME)
            .authority(Constants.NOOB_HOST)


        for ((key, value) in queries) {
            uriBuilder.appendQueryParameter(key, value)
        }
        uriBuilder.appendQueryParameter(Constants.IS_FROM_ANDROID, "1")
        return uriBuilder.build().toString()
    }

    fun handleDeepLink(uri: Uri?): Boolean {
        uri?.let { path ->
            val newPrefValues = mutableMapOf<String, String>()
            val queries = path.queryParameterNames.toList()

            queries.forEach { key ->
                val value = path.getQueryParameter(key)
                value?.let {
                    newPrefValues[key] = it
                }
            }
            NoobHelper.prefManager.importAccount(newPrefValues)

            return true
        }
        return false
    }
}