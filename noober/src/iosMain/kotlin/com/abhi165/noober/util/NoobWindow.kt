package com.abhi165.noober.util

import com.abhi165.noober.Noober
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRect
import platform.UIKit.UIEvent
import platform.UIKit.UIEventSubtype
import platform.UIKit.UIEventSubtypeMotionShake
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene

@OptIn(ExperimentalForeignApi::class)
internal class NoobWindow : UIWindow {
    @OverrideInit
    constructor (
        frame: CValue<CGRect>
    ) : super(frame)

    constructor (
        scene: UIWindowScene
    ) : super(scene)


    override fun motionEnded(motion: UIEventSubtype, withEvent: UIEvent?) {
        super.motionEnded(motion, withEvent)
        if (motion == UIEventSubtypeMotionShake) {
            Noober.toggle()
        }
    }
}