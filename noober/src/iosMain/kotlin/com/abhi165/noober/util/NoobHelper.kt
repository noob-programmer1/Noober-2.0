package com.abhi165.noober.util

import com.abhi165.noober.ui.NoobScreen
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import moe.tlaster.precompose.PreComposeApplication
import platform.Foundation.NSURLProtocol
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIModalPresentationFullScreen
import platform.UIKit.UIViewController
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
internal object NoobHelper {
    private var hasAlreadyStarted = false
    private var isNoobAlreadyShowing = false

    private val topViewController: UIViewController?
        get() {
            var rootController = UIApplication.sharedApplication.keyWindow?.rootViewController
            while (rootController?.presentedViewController != null) {
                rootController = rootController.presentedViewController
            }
            return rootController
        }

    @OptIn(ExperimentalForeignApi::class)
    private val noobController: UIViewController by lazy {
        var topSafeArea = 0f
        UIApplication.sharedApplication.keyWindow?.safeAreaInsets?.useContents {
            topSafeArea = this.top.toFloat()
        }
        PreComposeApplication { NoobScreen(topSafeArea = topSafeArea) }
    }


    fun toggle() {
        if (!isNoobAlreadyShowing) {
            noobController.modalPresentationStyle = UIModalPresentationFullScreen
            topViewController?.presentViewController(noobController, true, null)
        } else {
            noobController.dismissViewControllerAnimated(true, completion = null)
        }
        isNoobAlreadyShowing = !isNoobAlreadyShowing
    }

    fun share(data: String) {
        val activityViewController = UIActivityViewController(listOf(data), null)
        topViewController?.presentViewController(activityViewController, true, null)
    }

    @OptIn(BetaInteropApi::class)
    fun registerProtocol() {
        if (hasAlreadyStarted) return
        hasAlreadyStarted = true
        NoobProtocol.`class`()?.let { NSURLProtocol.registerClass(it) }
    }
}