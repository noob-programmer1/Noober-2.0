package com.abhi165.noober.model

data class NoobUserProperties(
    val key: String,
    val alternateKeyForCrossPlatform: String = key
)
