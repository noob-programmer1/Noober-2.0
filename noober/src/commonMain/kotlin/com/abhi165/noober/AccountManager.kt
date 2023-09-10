package com.abhi165.noober

internal interface AccountManager {
     suspend fun generateDeepLink(): String
     fun restoreAccount()
}