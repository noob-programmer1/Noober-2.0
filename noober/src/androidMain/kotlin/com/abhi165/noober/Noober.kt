package com.abhi165.noober

import android.content.Context

actual object Noober : NooberCommon {
    fun start(context: Context) {
        NoobHelper.init(context)
    }
}

internal actual fun getSharedPrefManager(): SharedPrefManager = NoobHelper.prefManager
internal actual fun isAndroid(): Boolean = true
internal actual fun share(data: String) {
    NoobHelper.share(data)
}
internal actual fun getAccountManager(): AccountManager = AccountManagerImpl
