package com.abhi165.noober.util

import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents
import platform.Foundation.NSURLQueryItem

internal object DeepLinkHandler {

    fun generateDeepLink(userProp: Map<String, String>): String {
        val urlComponent = NSURLComponents().apply {
            setScheme(Constants.NOOB_SCHEME)
            setHost(Constants.NOOB_HOST)
        }

        val queryItems = userProp.map { (key, value) ->
            NSURLQueryItem(key, value)
        }.toMutableList()
        queryItems.add(NSURLQueryItem(Constants.IS_FROM_ANDROID, "0"))
        urlComponent.queryItems = queryItems

        return urlComponent.URL?.absoluteString ?: ""
    }

    fun handleDeepLink(url: NSURL) {
        if (url.host() != Constants.NOOB_HOST) return
        val parameters = mutableMapOf<String, String>()
        NSURLComponents(url, false).queryItems?.map { it as? NSURLQueryItem }?.forEach {
            it?.let { query ->
                parameters[query.name] = query.value ?: ""
            }
        }
        UserDefaultManager.importAccount(parameters)
    }
}