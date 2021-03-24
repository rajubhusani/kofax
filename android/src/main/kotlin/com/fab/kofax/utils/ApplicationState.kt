package com.fab.kofax.utils

import com.fab.kofax.component.Component
import com.fab.kofax.utils.Enums.CaptureState
import com.fab.kofax.utils.Enums.ImageStatus
import java.io.Serializable

class ApplicationState : Serializable {
    @JvmField
    var mFrontState = ImageStatus.NONE
    @JvmField
    var mBackState = ImageStatus.NONE
    @JvmField
    var mCaptureState = CaptureState.FRONT
    @JvmField
    var mCaptureActivityDispalyed = false
    @JvmField
    var mPositionCoordinates: Array<Array<Int>>? = null
    @JvmField
    var mActiveComponent: Component? = null
}