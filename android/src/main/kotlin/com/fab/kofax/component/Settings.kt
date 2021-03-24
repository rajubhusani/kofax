package com.fab.kofax.component

import android.util.Log
import com.fab.kofax.common.JSONKeys
import com.fab.kofax.utils.*
import org.json.JSONException
import org.json.JSONObject

class Settings {
    @JvmField
    var camerasettings: SettingsCamera
    @JvmField
    var evrsSettings: SettingsEVRS
    var extractionSettings: SettingsExtraction
    @JvmField
    var advancedSettings: SettingsAdvanced
    fun convertFromJson(jsonComponent: JSONObject?) {
        try {
            if (jsonComponent == null) {
                return
            }


            // Settings
            val jsonSettings = jsonComponent.getJSONObject(JSONKeys.SETTINGS)

            // Camera Settings
            val jsonCameraSettings = jsonSettings.optJSONObject(JSONKeys.CAMERA_SETTINGS)
            if (jsonCameraSettings != null) {
                camerasettings.CaptureExperience = jsonCameraSettings.optInt(JSONKeys.CAPTURE_EXPERIENCEMODE, Enums.CaptureExperienceMode.DOCUMENTCAPTURE.ordinal)
                camerasettings.OffsetThreshold = jsonCameraSettings.optInt(JSONKeys.OFFSET_THRESHOLD)
                camerasettings.StabilityDelay = jsonCameraSettings.optInt(JSONKeys.STABILITYDELAY)
                camerasettings.PitchThreshold = jsonCameraSettings.optInt(JSONKeys.PITCH_THRESHOLD)
                camerasettings.RollThreshold = jsonCameraSettings.optInt(JSONKeys.ROLL_THRESHOLD)
                camerasettings.ManualCaptureTimer = jsonCameraSettings.optInt(JSONKeys.MANUAL_CAPTURE_TIMER)
                camerasettings.AspectRatio = jsonCameraSettings.optDouble(JSONKeys.ASPECT_RATIO, 8.5f / 11f.toDouble()).toFloat()
                camerasettings.TargetFramePadding = jsonCameraSettings.optDouble(JSONKeys.TARGET_FRAME_PADDING, 9.0)
                camerasettings.CaptureMode = Enums.CaptureMode.values()[jsonCameraSettings.optInt(JSONKeys.CAPTURE_MODE, 1)]
                val detector = jsonCameraSettings.optInt(JSONKeys.DETECTOR, 1)
                camerasettings.Detector = Enums.Detector.values()[if (detector > 1) 1 else detector]
                camerasettings.Orientation = jsonCameraSettings.optInt(JSONKeys.ORIENTATION)
                camerasettings.ShowGallery = jsonCameraSettings.optBoolean(JSONKeys.SHOW_GALLERY, false)
                camerasettings.MaxFillFraction = jsonCameraSettings.optDouble(JSONKeys.MAX_FILL_FRACTION, 1.1).toFloat()
                camerasettings.MinFillFraction = jsonCameraSettings.optDouble(JSONKeys.MIN_FILL_FRACTION, 0.65).toFloat()
                camerasettings.MaxSkewAngle = jsonCameraSettings.optDouble(JSONKeys.MAX_SKEW_ANGLE, 10.0).toFloat()
                camerasettings.ToleranceFranction = jsonCameraSettings.optDouble(JSONKeys.TOLERANCE_FRACTION, 0.15).toFloat()
                camerasettings.AutoTorch = jsonCameraSettings.optBoolean(JSONKeys.AUTO_TORCH, true)
                camerasettings.ShowCaptureDemo = jsonCameraSettings.optBoolean(JSONKeys.SHOW_CAPTURE_DEMO, true)
                camerasettings.ShowPageDetection = jsonCameraSettings.optBoolean(JSONKeys.SHOW_PAGE_DETECTION, false)
                camerasettings.ShowDiagnostics = jsonCameraSettings.optBoolean(JSONKeys.SHOW_CAPTURE_DIAGNOSTICS, false)
                camerasettings.enableGlareDetection = jsonCameraSettings.optBoolean(JSONKeys.ENABLE_GLARE_DETECTION, false)
                camerasettings.enableGlareExperience = jsonCameraSettings.optBoolean(JSONKeys.ENABLE_GLARE_EXPERIENCE, false)
                camerasettings.tiltAngle = jsonCameraSettings.optInt(JSONKeys.TILT_ANGLE, 12)
            }


            // EVRS Settings
            val jsonEVRSSettings = jsonSettings.optJSONObject(JSONKeys.EVRS_SETTINGS)
            if (jsonEVRSSettings != null) {
                evrsSettings.UseDefaultIPProfile = jsonEVRSSettings.optBoolean(JSONKeys.USE_BANKRIGHT_SETTINGS)
                evrsSettings.UseQuickAnalysis = jsonEVRSSettings.optBoolean(JSONKeys.DO_QUICKANALYSIS)
                evrsSettings.IPString = jsonEVRSSettings.optString(JSONKeys.IP_String)
                evrsSettings.CheckBackIPString = jsonEVRSSettings.optString(JSONKeys.CheckBackIPString)
                evrsSettings.ProcessType = jsonEVRSSettings.optInt(JSONKeys.PROCESS_TYPE)
                evrsSettings.SendImageSummary = jsonEVRSSettings.optBoolean(JSONKeys.SEND_IMAGE_SUMMARY, false)
            }

            // Advanced Settings
            val jsonAdvancedSettings = jsonSettings
                    .optJSONObject(JSONKeys.ADVANCED_SETTINGS)
            if (jsonAdvancedSettings != null) {
                advancedSettings.SearchMICR = jsonAdvancedSettings.optBoolean(JSONKeys.SEARCH_MICR)
                advancedSettings.UseHandPrint = jsonAdvancedSettings.optBoolean(JSONKeys.USE_HAND_PRINT)
                advancedSettings.CheckForDuplicates = jsonAdvancedSettings.optBoolean(JSONKeys.CHECK_FOR_DUPLICATES)
                advancedSettings.CheckValidationAtServer = jsonAdvancedSettings.optInt(JSONKeys.CHECK_VALIDATION_AT_SERVER, 0) == 1
                advancedSettings.ShowCheckInfo = jsonAdvancedSettings.optBoolean(JSONKeys.SHOW_CHECK_INFO)
                advancedSettings.CheckExtraction = jsonAdvancedSettings.optInt(JSONKeys.CHECK_EXTRACTION, 1)
                advancedSettings.ShowCaptureDemo = jsonAdvancedSettings.optBoolean(JSONKeys.SHOW_CAPTURE_DEMO, false)
                advancedSettings.isFirstTimeCheckDemo = jsonAdvancedSettings.optBoolean(JSONKeys.IS_FIRST_TIME_CHECK_DEMO, true)
                // Custom Component
                advancedSettings.NumberOfDocumentsToCapture = jsonAdvancedSettings.optInt(JSONKeys.NUMBER_OF_DOCUMENTS_TO_CAPTURE, 1)
            }
        } catch (ex: JSONException) {
            ex.printStackTrace()
            Log.e("IO excetion", "Error Message :" + ex.message)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.e("IO excetion", "Error Message :" + ex.message)
        }
    }

    fun convertToJSONObject(): JSONObject {
        val jsonSettings = JSONObject()
        try {
            // Camera Settings
            val jsonCameraSettings = JSONObject()
            jsonCameraSettings.put(JSONKeys.CAPTURE_EXPERIENCEMODE, camerasettings.CaptureExperience)
            jsonCameraSettings.put(JSONKeys.OFFSET_THRESHOLD, camerasettings.OffsetThreshold)
            jsonCameraSettings.put(JSONKeys.STABILITYDELAY, camerasettings.StabilityDelay)
            jsonCameraSettings.put(JSONKeys.PITCH_THRESHOLD, camerasettings.PitchThreshold)
            jsonCameraSettings.put(JSONKeys.ROLL_THRESHOLD, camerasettings.RollThreshold)
            jsonCameraSettings.put(JSONKeys.MANUAL_CAPTURE_TIMER, camerasettings.ManualCaptureTimer)
            jsonCameraSettings.put(JSONKeys.ASPECT_RATIO, camerasettings.AspectRatio)
            jsonCameraSettings.put(JSONKeys.TARGET_FRAME_PADDING, camerasettings.TargetFramePadding)
            jsonCameraSettings.put(JSONKeys.CAPTURE_MODE, if (camerasettings.CaptureMode != null) camerasettings.CaptureMode!!.ordinal else 0)
            jsonCameraSettings.put(JSONKeys.DETECTOR, if (camerasettings.Detector != null) camerasettings.Detector!!.ordinal else 0)
            jsonCameraSettings.put(JSONKeys.ORIENTATION, camerasettings.Orientation)
            jsonCameraSettings.put(JSONKeys.SHOW_GALLERY, camerasettings.ShowGallery)
            jsonCameraSettings.put(JSONKeys.AUTO_TORCH, camerasettings.AutoTorch)
            jsonCameraSettings.put(JSONKeys.SHOW_CAPTURE_DEMO, camerasettings.ShowCaptureDemo)
            jsonCameraSettings.put(JSONKeys.TOLERANCE_FRACTION, camerasettings.ToleranceFranction)
            jsonCameraSettings.put(JSONKeys.MAX_SKEW_ANGLE, camerasettings.MaxSkewAngle)
            jsonCameraSettings.put(JSONKeys.MIN_FILL_FRACTION, camerasettings.MinFillFraction)
            jsonCameraSettings.put(JSONKeys.MAX_FILL_FRACTION, camerasettings.MaxFillFraction)
            jsonCameraSettings.put(JSONKeys.SHOW_PAGE_DETECTION, camerasettings.ShowPageDetection)
            jsonCameraSettings.put(JSONKeys.SHOW_CAPTURE_DIAGNOSTICS, camerasettings.ShowDiagnostics)
            jsonCameraSettings.put(JSONKeys.ENABLE_GLARE_DETECTION, camerasettings.enableGlareDetection)
            jsonCameraSettings.put(JSONKeys.ENABLE_GLARE_EXPERIENCE, camerasettings.enableGlareExperience)
            jsonCameraSettings.put(JSONKeys.TILT_ANGLE, camerasettings.tiltAngle)
            jsonSettings.put(JSONKeys.CAMERA_SETTINGS, jsonCameraSettings)

            // EVRS Settings
            val jsonEVRSSettings = JSONObject()
            jsonEVRSSettings.put(JSONKeys.USE_BANKRIGHT_SETTINGS, evrsSettings.UseDefaultIPProfile)
            jsonEVRSSettings.put(JSONKeys.DO_QUICKANALYSIS, evrsSettings.UseQuickAnalysis)
            jsonEVRSSettings.put(JSONKeys.IP_String, evrsSettings.IPString)
            jsonEVRSSettings.put(JSONKeys.CheckBackIPString, evrsSettings.CheckBackIPString)
            jsonEVRSSettings.put(JSONKeys.PROCESS_TYPE, evrsSettings.ProcessType)
            jsonEVRSSettings.put(JSONKeys.SEND_IMAGE_SUMMARY, evrsSettings.SendImageSummary)
            jsonSettings.put(JSONKeys.EVRS_SETTINGS, jsonEVRSSettings)
            val jsonAdvancedSettings = JSONObject()
            jsonAdvancedSettings.put(JSONKeys.SEARCH_MICR, advancedSettings.SearchMICR)
            jsonAdvancedSettings.put(JSONKeys.USE_HAND_PRINT, advancedSettings.UseHandPrint)
            jsonAdvancedSettings.put(JSONKeys.CHECK_FOR_DUPLICATES, advancedSettings.CheckForDuplicates)
            jsonAdvancedSettings.put(JSONKeys.CHECK_VALIDATION_AT_SERVER, if (advancedSettings.CheckValidationAtServer) 1 else 0)
            jsonAdvancedSettings.put(JSONKeys.SHOW_CHECK_INFO, advancedSettings.ShowCheckInfo)
            jsonAdvancedSettings.put(JSONKeys.CHECK_EXTRACTION, advancedSettings.CheckExtraction)
            jsonAdvancedSettings.put(JSONKeys.NUMBER_OF_DOCUMENTS_TO_CAPTURE, advancedSettings.NumberOfDocumentsToCapture)
            jsonAdvancedSettings.put(JSONKeys.SHOW_CAPTURE_DEMO, advancedSettings.ShowCaptureDemo)
            jsonAdvancedSettings.put(JSONKeys.IS_FIRST_TIME_CHECK_DEMO, advancedSettings.isFirstTimeCheckDemo)
            jsonSettings.put(JSONKeys.ADVANCED_SETTINGS, jsonAdvancedSettings)
        } catch (ex: JSONException) {
            ex.printStackTrace()
            Log.e("IO excetion", "Error Message :" + ex.message)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.e("IO excetion", "Error Message :" + ex.message)
        }
        return jsonSettings
    }

    init {
        camerasettings = SettingsCamera()
        evrsSettings = SettingsEVRS()
        extractionSettings = SettingsExtraction()
        advancedSettings = SettingsAdvanced()
    }
}