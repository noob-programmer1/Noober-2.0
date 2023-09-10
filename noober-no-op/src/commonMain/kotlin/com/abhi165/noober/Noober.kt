package com.abhi165.noober

import com.abhi165.noober.model.NoobUserProperties
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

interface NooberCommon {
    fun log(tag:String, value: Any, isError: Boolean = false) {}

    @OptIn(ExperimentalObjCName::class)
    fun setUserProperties(@ObjCName(name = "_") prop: List<NoobUserProperties>) {}

    fun mock(url: String, json: Map<Any, Any>) {}
    fun intercept(url: String) {}
}

expect object Noober: NooberCommon

