package com.fab.kofax.component

import android.util.Log
import com.fab.kofax.common.JSONKeys
import org.json.JSONException
import org.json.JSONObject

class Localization : ILocalization {
    var Instructions: InstructionsLocalization
    var Camera: CameraLocalization
    var Preview: PreviewLocalization
    override fun ConvertFromJson(jsonComponent: JSONObject?): Localization? {
        Instructions = InstructionsLocalization()
        Camera = CameraLocalization()
        Preview = PreviewLocalization()
        try {
            if (jsonComponent == null) {
                return this
            }
            val defaultResourceText = InstructionTexts()
            val jsonLocalization = jsonComponent.getJSONObject(JSONKeys.SCREEN_TEXTS)

            // Instructions Localization
            val jsonInstructions = jsonLocalization.getJSONObject(JSONKeys.SUMMARY)
            Instructions.Is_InstructionText_Changed = jsonInstructions.optBoolean(JSONKeys.IS_INSTRUCTION_TEXT_CHANGED)
            Instructions.Is_SubmitButtonText_Changed = jsonInstructions.optBoolean(JSONKeys.IS_SUBMIT_BUTTON_TEXT_CHANGED)
            Instructions.Is_SubmitAlertText_Changed = jsonInstructions.optBoolean(JSONKeys.IS_SUBMIT_ALERT_TEXT_CHANGED)
            Instructions.Is_SubmitCancelAlertText_Changed = jsonInstructions.optBoolean(JSONKeys.IS_SUBMIT_CANCEL_ALERT_TEXT_CHANGED)

            // Camera Localization
            val jsonCamera = jsonLocalization.getJSONObject(JSONKeys.CAMERA)
            Camera.Is_mUEUserInstructionFront_Changed = jsonCamera.optBoolean(JSONKeys.IS_UE_USER_INSTRUCTION_FRONT_CHANGED)
            Camera.Is_mUEUserInstructionBack_Changed = jsonCamera.optBoolean(JSONKeys.IS_UE_USER_INSTRUCTION_BACK_CHANGED)
            Camera.Is_mUEMoveCloser_Changed = jsonCamera.optBoolean(JSONKeys.IS_UE_MOVE_CLOSER_CHANGED)
            Camera.Is_mUEMoveBack_Changed = jsonCamera.optBoolean(JSONKeys.IS_UE_MOVE_BACK_CHANGED)
            Camera.Is_mUEHoldSteady_Changed = jsonCamera.optBoolean(JSONKeys.IS_UE_HOLD_STEADY_CHANGED)
            Camera.Is_mUECenter_Changed = jsonCamera.optBoolean(JSONKeys.IS_UE_CENTER_CHANGED)
            Camera.Is_mUECaptured_Changed = jsonCamera.optBoolean(JSONKeys.IS_UE_CAPTURED_CHANGED)
            Camera.Is_mUEHoldParallel_text_Changed = jsonCamera.optBoolean(JSONKeys.IS_UE_HOLD_PARALLEL_TEXT_CHANGED)
            Camera.Is_mUEOrientation_text_Changed = jsonCamera.optBoolean(JSONKeys.IS_UE_ORIENTATION_TEXT_CHANGED)
            Camera.Is_mUETiltUpDevice_Changed = jsonCamera.optBoolean(JSONKeys.IS_UE_TILT_DEVICE_UP_TEXT_CHANGED)
            Camera.Is_mUETiltForwardDevice_Changed = jsonCamera.optBoolean(JSONKeys.IS_UE_TILT_DEVICE_FORWARD_TEXT_CHANGED)

            // Preview Localization
            val jsonPreview = jsonLocalization.getJSONObject(JSONKeys.PREVIEW)
            Preview.Is_FrontUseButton_Changed = jsonPreview.optBoolean(JSONKeys.IS_FRONT_USE_BUTTON_CHANGED)
            Preview.Is_FrontRetakeButton_Changed = jsonPreview.optBoolean(JSONKeys.IS_FRONT_RETAKE_BUTTON_CHANGED)
            Preview.Is_CancelButton_Changed = jsonPreview.optBoolean(JSONKeys.IS_CANCEL_BUTTON_CHANGED)
            Instructions.InstructionText = getLocalizationText(Instructions.Is_InstructionText_Changed, jsonInstructions.optString(JSONKeys.INSTRUCTION_TEXT), defaultResourceText.InstructionText)
            Instructions.SubmitButtonText = getLocalizationText(Instructions.Is_SubmitButtonText_Changed, jsonInstructions.optString(JSONKeys.SUBMIT_BUTTON_TEXT), defaultResourceText.SubmitButtonText)
            Instructions.SubmitAlertText = getLocalizationText(Instructions.Is_SubmitAlertText_Changed, jsonInstructions.optString(JSONKeys.SUBMIT_ALERT_TEXT), defaultResourceText.SubmitAlertText)
            Instructions.SubmitCancelAlertText = getLocalizationText(Instructions.Is_SubmitCancelAlertText_Changed, jsonInstructions.optString(JSONKeys.SUBMIT_CANCEL_ALERT_TEXT), defaultResourceText.SubmitCancelAlertText)
            Camera.uEUserInstructionFront = getLocalizationText(Camera.Is_mUEUserInstructionFront_Changed, jsonCamera.optString(JSONKeys.UE_USER_INSTRUCTION_FRONT), defaultResourceText.UserInstructionFront)
            Camera.uEMoveCloser = getLocalizationText(Camera.Is_mUEMoveCloser_Changed, jsonCamera.optString(JSONKeys.UE_MOVE_CLOSER), defaultResourceText.MoveCloserText)
            Camera.uEMoveBack = getLocalizationText(Camera.Is_mUEMoveBack_Changed, jsonCamera.optString(JSONKeys.UE_MOVE_BACK), defaultResourceText.MoveBackText)
            Camera.uEHoldSteady = getLocalizationText(Camera.Is_mUEHoldSteady_Changed, jsonCamera.optString(JSONKeys.UE_HOLD_STEADY), defaultResourceText.HoldSteadyText)
            Camera.uECenter = getLocalizationText(Camera.Is_mUECenter_Changed, jsonCamera.optString(JSONKeys.UE_CENTER), defaultResourceText.CenterText)
            Camera.uECaptured = getLocalizationText(Camera.Is_mUECaptured_Changed, jsonCamera.optString(JSONKeys.UE_CAPTURED), defaultResourceText.CapturedText)
            Camera.uETiltUpDevice = getLocalizationText(Camera.Is_mUETiltUpDevice_Changed, jsonCamera.optString(JSONKeys.UE_TILT_DEVICE_UP_TEXT), defaultResourceText.tiltDeviceUpText)
            Camera.uETiltForwardDevice = getLocalizationText(Camera.Is_mUETiltUpDevice_Changed, jsonCamera.optString(JSONKeys.UE_TILT_DEVICE_FORWARD_TEXT), defaultResourceText.tiltDeviceForwardText)
            Camera.uEHoldParallel_text = getLocalizationText(Camera.Is_mUEHoldParallel_text_Changed, jsonCamera.optString(JSONKeys.UE_HOLD_PARALLEL_TEXT), defaultResourceText.HoldParallelText)
            Camera.uEOrientation_text = getLocalizationText(Camera.Is_mUEOrientation_text_Changed, jsonCamera.optString(JSONKeys.UE_ORIENTATION_TEXT), defaultResourceText.OrientationText)
            Preview.FrontUseButton = getLocalizationText(Preview.Is_FrontUseButton_Changed, jsonPreview.optString(JSONKeys.FRONT_USE_BUTTON), defaultResourceText.FrontUseButtonText)
            Preview.FrontRetakeButton = getLocalizationText(Preview.Is_FrontRetakeButton_Changed, jsonPreview.optString(JSONKeys.FRONT_RETAKE_BUTTON), defaultResourceText.FrontRetakeButtonText)
            Preview.CancelButton = getLocalizationText(Preview.Is_CancelButton_Changed, jsonPreview.optString(JSONKeys.CANCEL_BUTTON), defaultResourceText.CancelButtonText)
        } catch (ex: JSONException) {
            ex.printStackTrace()
            Log.e("IO excetion", "Error Message :" + ex.message)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.e("IO excetion", "Error Message :" + ex.message)
        }
        return this
    }

    override fun ConvertToJSONObject(isForExport: Boolean): JSONObject? {
        val jsonLocalization = JSONObject()
        try {
            // Instructions Localization
            val jsonInstructions = JSONObject()
            jsonInstructions.put(JSONKeys.INSTRUCTION_TEXT, Instructions.InstructionText)
            jsonInstructions.put(JSONKeys.SUBMIT_BUTTON_TEXT, Instructions.SubmitButtonText)
            jsonInstructions.put(JSONKeys.SUBMIT_ALERT_TEXT, Instructions.SubmitAlertText)
            jsonInstructions.put(JSONKeys.SUBMIT_CANCEL_ALERT_TEXT, Instructions.SubmitCancelAlertText)

            // Camera Localization
            val jsonCamera = JSONObject()
            jsonCamera.put(JSONKeys.UE_USER_INSTRUCTION_FRONT, Camera.uEUserInstructionFront)
            jsonCamera.put(JSONKeys.UE_MOVE_CLOSER, Camera.uEMoveCloser)
            jsonCamera.put(JSONKeys.UE_MOVE_BACK, Camera.uEMoveBack)
            jsonCamera.put(JSONKeys.UE_HOLD_STEADY, Camera.uEHoldSteady)
            jsonCamera.put(JSONKeys.UE_CENTER, Camera.uECenter)
            jsonCamera.put(JSONKeys.UE_CAPTURED, Camera.uECaptured)
            jsonCamera.put(JSONKeys.UE_CENTER, Camera.uECenter)
            jsonCamera.put(JSONKeys.UE_CAPTURED, Camera.uECaptured)
            jsonCamera.put(JSONKeys.UE_HOLD_PARALLEL_TEXT, Camera.uEHoldParallel_text)
            jsonCamera.put(JSONKeys.UE_ORIENTATION_TEXT, Camera.uEOrientation_text)
            jsonCamera.put(JSONKeys.UE_TILT_DEVICE_UP_TEXT, Camera.uETiltUpDevice)
            jsonCamera.put(JSONKeys.UE_TILT_DEVICE_FORWARD_TEXT, Camera.uETiltForwardDevice)


            // Preview Localization
            val jsonPreview = JSONObject()
            jsonPreview.put(JSONKeys.FRONT_USE_BUTTON, Preview.FrontUseButton)
            jsonPreview.put(JSONKeys.FRONT_RETAKE_BUTTON, Preview.FrontRetakeButton)
            jsonPreview.put(JSONKeys.CANCEL_BUTTON, Preview.CancelButton)
            if (!isForExport) {
                jsonInstructions.put(JSONKeys.IS_INSTRUCTION_TEXT_CHANGED, Instructions.Is_InstructionText_Changed)
                jsonInstructions.put(JSONKeys.IS_SUBMIT_BUTTON_TEXT_CHANGED, Instructions.Is_SubmitButtonText_Changed)
                jsonInstructions.put(JSONKeys.IS_SUBMIT_ALERT_TEXT_CHANGED, Instructions.Is_SubmitAlertText_Changed)
                jsonInstructions.put(JSONKeys.IS_SUBMIT_CANCEL_ALERT_TEXT_CHANGED, Instructions.Is_SubmitCancelAlertText_Changed)
                jsonCamera.put(JSONKeys.IS_UE_USER_INSTRUCTION_FRONT_CHANGED, Camera.Is_mUEUserInstructionFront_Changed)
                jsonCamera.put(JSONKeys.IS_UE_USER_INSTRUCTION_BACK_CHANGED, Camera.Is_mUEUserInstructionBack_Changed)
                jsonCamera.put(JSONKeys.IS_UE_MOVE_CLOSER_CHANGED, Camera.Is_mUEMoveCloser_Changed)
                jsonCamera.put(JSONKeys.IS_UE_MOVE_BACK_CHANGED, Camera.Is_mUEMoveBack_Changed)
                jsonCamera.put(JSONKeys.IS_UE_HOLD_STEADY_CHANGED, Camera.Is_mUEHoldSteady_Changed)
                jsonCamera.put(JSONKeys.IS_UE_CENTER_CHANGED, Camera.Is_mUECenter_Changed)
                jsonCamera.put(JSONKeys.IS_UE_CAPTURED_CHANGED, Camera.Is_mUECaptured_Changed)
                jsonCamera.put(JSONKeys.IS_UE_HOLD_PARALLEL_TEXT_CHANGED, Camera.Is_mUEHoldParallel_text_Changed)
                jsonCamera.put(JSONKeys.IS_UE_ORIENTATION_TEXT_CHANGED, Camera.Is_mUEOrientation_text_Changed)
                jsonCamera.put(JSONKeys.IS_UE_TILT_DEVICE_UP_TEXT_CHANGED, Camera.Is_mUETiltUpDevice_Changed)
                jsonCamera.put(JSONKeys.IS_UE_TILT_DEVICE_FORWARD_TEXT_CHANGED, Camera.Is_mUETiltForwardDevice_Changed)
                jsonPreview.put(JSONKeys.IS_FRONT_USE_BUTTON_CHANGED, Preview.Is_FrontUseButton_Changed)
                jsonPreview.put(JSONKeys.IS_FRONT_RETAKE_BUTTON_CHANGED, Preview.Is_FrontRetakeButton_Changed)
                jsonPreview.put(JSONKeys.IS_CANCEL_BUTTON_CHANGED, Preview.Is_CancelButton_Changed)
            }
            jsonLocalization.put(JSONKeys.SUMMARY, jsonInstructions)
            jsonLocalization.put(JSONKeys.CAMERA, jsonCamera)
            jsonLocalization.put(JSONKeys.PREVIEW, jsonPreview)
        } catch (ex: JSONException) {
            ex.printStackTrace()
            Log.e("IO excetion", "Error Message :" + ex.message)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.e("IO excetion", "Error Message :" + ex.message)
        }
        return jsonLocalization
    }

    internal inner class InstructionTexts {
        var InstructionText = ""
        var SubmitButtonText = ""
        var SubmitAlertText = ""
        var SubmitCancelAlertText = ""
        var UserInstructionFront = ""
        var MoveCloserText = ""
        var MoveBackText = ""
        var HoldSteadyText = ""
        var CenterText = ""
        var CapturedText = ""
        var HoldParallelText = ""
        var OrientationText = ""
        var FrontUseButtonText = ""
        var FrontRetakeButtonText = ""
        var CancelButtonText = ""
        var tiltDeviceUpText = ""
        var tiltDeviceForwardText = ""
    }

    companion object {
        /***
         * This method returns the original string when text has modified otherwise returns the localized text.
         * @param isTextChanged
         * @param originalText
         * @param LocalizedText
         * @return : Based on isTextChanged flag its returns the Original/Localized String.
         */
        fun getLocalizationText(isTextChanged: Boolean, originalText: String, LocalizedText: String): String {
            return if (isTextChanged) {
                originalText
            } else {
                LocalizedText
            }
        }
    }

    init {
        Instructions = InstructionsLocalization()
        Camera = CameraLocalization()
        Preview = PreviewLocalization()
    }
}