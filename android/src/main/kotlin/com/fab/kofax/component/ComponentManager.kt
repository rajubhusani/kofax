package com.fab.kofax.component

import android.content.Context
import com.fab.kofax.utils.Enums

class ComponentManager(ctx: Context?) {
    val PBRTTIServerUrl = "https://mobiledemo.kofax.com/mobilesdk/api/billpay?customer=Kofax"
    val CDRTTIServerUrl = "https://mobiledemo.kofax.com/mobilesdk/api/CheckDeposit?customer=Kofax"

    // 2.0 Url
    val IDRTTIServerUrl = "https://mobiledemo.kofax.com/mobilesdk/api/MobileID_2X"
    val CCRTTIServerUrl = "https://mobiledemo.kofax.com/mobilesdk/api/MobileID_2X?IDtype=id"

    // 2.0 Url
    val KTAKofaxServerUrl = "https://mobiledemo4.kofax.com/TotalAgility/Services/SDK/"

    // 1.5 Url
    val KTAServerUrl = ""
    val AuthenticKTAServerUrl = "https://mobiledemo4.kofax.com/TotalAgility/Services/SDK/"
    val ODELicenseRTTIServerUrl = "https://mobiledemo.kofax.com/mobilesdk"
    val CCardRTTIServerUrl = "https://mobiledemo.kofax.com/mobilesdk/api/cardcapture"
    val ODELicenseKTAServerUrl = ""
    val ODEModeldownloadUrl = "https://mobiledemo.kofax.com/mobileupdater/api/ExtractionModelService"
    val KTASessionID = "C640521793431F4486D4EF1586672385"
    val AuthenticKTASessionID = "C640521793431F4486D4EF1586672385"
    val KTAServerDeviceSessionID = "C640521793431F4486D4EF1586672385"
    val KTAPBProcessName = "KofaxBillPaySync"
    val KTAMIDServerProcessName = "KofaxMobileIDSync"
    val KTAMIDAuthenticProcessName = "KofaxMobileIDCaptureSync"
    val KTAMIDServerDeviceProcessName = "KofaxMobileIDCaptureSync"
    val KTACCProcessName = "KofaxCardCaptureSync"
    val MIDType = "ID"
    val KTACDProcessName = "KofaxCheckDepositSync"
    var mcontext: Context? = null
    val component: Component
        get() = defaultCDComponent

    // Camera Settings
    //0.45f;

    // EVRS Settings


    // Advanced Settings
    private val defaultCDComponent: CheckDepositComponent
        get() {
            val cdcomponent = CheckDepositComponent()
            cdcomponent.settings = Settings()

            // Camera Settings
            cdcomponent.settings!!.camerasettings.ISFIRSTLAUNCH = true
            cdcomponent.settings!!.camerasettings.CaptureExperience = Enums.CaptureExperienceMode.CHECKCAPTURE.ordinal
            cdcomponent.settings!!.camerasettings.OffsetThreshold = 85
            cdcomponent.settings!!.camerasettings.StabilityDelay = 75
            cdcomponent.settings!!.camerasettings.PitchThreshold = 15
            cdcomponent.settings!!.camerasettings.RollThreshold = 15
            cdcomponent.settings!!.camerasettings.ManualCaptureTimer = 15
            cdcomponent.settings!!.camerasettings.AspectRatio = 6.0f / 2.75f //0.45f;
            cdcomponent.settings!!.camerasettings.TargetFramePadding = 9.0
            cdcomponent.settings!!.camerasettings.CaptureMode = Enums.CaptureMode.VIDEO
            cdcomponent.settings!!.camerasettings.Detector = Enums.Detector.ISG
            cdcomponent.settings!!.camerasettings.Orientation = Enums.Orientation.LANDSCAPE.ordinal
            cdcomponent.settings!!.camerasettings.ShowGallery = false
            cdcomponent.settings!!.camerasettings.MaxFillFraction = 1.1f
            cdcomponent.settings!!.camerasettings.MinFillFraction = 0.65.toFloat()
            cdcomponent.settings!!.camerasettings.MaxSkewAngle = 10.toFloat()
            cdcomponent.settings!!.camerasettings.ToleranceFranction = 0.15.toFloat()
            cdcomponent.settings!!.camerasettings.AutoTorch = false
            cdcomponent.settings!!.camerasettings.ShowCaptureDemo = true
            cdcomponent.settings!!.camerasettings.ShowPageDetection = false
            cdcomponent.settings!!.camerasettings.ShowDiagnostics = false

            // EVRS Settings
            cdcomponent.settings!!.evrsSettings.UseDefaultIPProfile = true
            cdcomponent.settings!!.evrsSettings.UseQuickAnalysis = false
            cdcomponent.settings!!.evrsSettings.ProcessType = 0
            cdcomponent.settings!!.evrsSettings.SendImageSummary = false


            // Advanced Settings
            cdcomponent.settings!!.advancedSettings.SearchMICR = true
            cdcomponent.settings!!.advancedSettings.UseHandPrint = true
            cdcomponent.settings!!.advancedSettings.CheckForDuplicates = true
            cdcomponent.settings!!.advancedSettings.CheckValidationAtServer = true
            cdcomponent.settings!!.advancedSettings.ShowCheckInfo = true
            cdcomponent.settings!!.advancedSettings.CheckExtraction = 2
            cdcomponent.settings!!.advancedSettings.ShowCaptureDemo = true
            return cdcomponent
        }

    init {
        mcontext = ctx
    }
}