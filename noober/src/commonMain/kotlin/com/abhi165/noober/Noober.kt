package com.abhi165.noober

import com.abhi165.noober.model.NoobUserProperties
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName


interface NooberCommon {
    fun log(tag:String, value: Any, isError: Boolean = false) {
        NoobRepository.log(tag, value, isError)
    }

    @OptIn(ExperimentalObjCName::class)
    fun setUserProperties(@ObjCName(name = "_") prop: List<NoobUserProperties>) {
        NoobRepository.addUserProperties(prop)
    }

    fun mock(url: String, json: Map<Any, Any>) {}
    fun intercept(url: String) {}
}

expect object Noober: NooberCommon
internal expect fun getSharedPrefManager(): SharedPrefManager
internal expect fun isAndroid(): Boolean
internal expect fun share(data: String)
internal expect fun getAccountManager(): AccountManager
