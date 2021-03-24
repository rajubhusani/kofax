package com.fab.kofax.utils

import com.fab.kofax.utils.Enums.CaptureMode
import com.fab.kofax.utils.Enums.Detector

class SettingsCamera {
    @JvmField
    var ISFIRSTLAUNCH = true
    @JvmField
    var CaptureExperience = 0
    var DocumentSettings = 0
    @JvmField
    var ManualCaptureTimer = 0

    // Thresholds
    @JvmField
    var OffsetThreshold = 0
    @JvmField
    var StabilityDelay = 0
    @JvmField
    var PitchThreshold = 0
    @JvmField
    var RollThreshold = 0
    @JvmField
    var AspectRatio = 0f
    @JvmField
    var TargetFramePadding = 0.0
    @JvmField
    var CaptureMode: CaptureMode? = null
    @JvmField
    var ShowGallery = false
    @JvmField
    var MaxFillFraction = 0f
    @JvmField
    var MinFillFraction = 0f
    @JvmField
    var MaxSkewAngle = 0f
    @JvmField
    var ToleranceFranction = 0f
    @JvmField
    var AutoTorch = false
    @JvmField
    var ShowCaptureDemo = false
    @JvmField
    var Detector: Detector? = null
    @JvmField
    var Orientation = 0
    @JvmField
    var ShowPageDetection = false
    @JvmField
    var ShowDiagnostics = false
    @JvmField
    var enableGlareDetection = false
    @JvmField
    var enableGlareExperience = false
    @JvmField
    var tiltAngle = 0
}