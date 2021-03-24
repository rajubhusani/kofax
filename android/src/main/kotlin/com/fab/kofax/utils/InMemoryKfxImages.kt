package com.fab.kofax.utils

import com.fab.kofax.common.CommonUtils.cleanupImageObj
import com.kofax.kmc.ken.engines.data.Image

object InMemoryKfxImages {
    @JvmField
    var mFrontRawImage: Image? = null
    @JvmField
    var mFrontRawPrimaryImage: Image? = null
    @JvmField
    var mBackRawImage: Image? = null
    @JvmField
    var mFrontProcessedImage: Image? = null
    @JvmField
    var mBackProcessedImage: Image? = null
    @JvmField
    var mMicrData: String? = null

    @JvmField
    var scanType: String? = null

    fun clearImages() {
        cleanupImageObj(mFrontRawImage)
        cleanupImageObj(mFrontRawPrimaryImage)
        cleanupImageObj(mBackRawImage)
        cleanupImageObj(mFrontProcessedImage)
        cleanupImageObj(mBackProcessedImage)
        mMicrData = null
    }
}