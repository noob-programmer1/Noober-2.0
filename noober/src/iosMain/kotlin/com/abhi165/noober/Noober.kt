package com.abhi165.noober

import com.abhi165.noober.util.DeepLinkHandler
import com.abhi165.noober.util.NoobHelper
import com.abhi165.noober.util.NoobProtocol
import com.abhi165.noober.util.NoobWindow
import com.abhi165.noober.util.UserDefaultManager
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRect
import platform.Foundation.NSURL
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene


actual object Noober: NooberCommon  {

    fun start() {
        NoobHelper.registerProtocol()
    }

    fun getNoobProtocol() = NoobProtocol.`class`()

    @OptIn(ExperimentalForeignApi::class)
    fun getNoobWindow(frame: CValue<CGRect>): UIWindow = NoobWindow(frame)
    @OptIn(ExperimentalForeignApi::class)
    fun getNoobWindow(scene: UIWindowScene): UIWindow = NoobWindow(scene)

    fun importAccountFromNoob(url: NSURL) {
        DeepLinkHandler.handleDeepLink(url)
    }

    fun toggle() {
        NoobHelper.toggle()
    }
}

internal actual fun getSharedPrefManager(): SharedPrefManager = UserDefaultManager
internal actual fun isAndroid(): Boolean = false
internal actual fun share(data: String) = NoobHelper.share(data)
internal actual fun getAccountManager(): AccountManager = AccountManagerImpl
