/** Copyright (c) 2012-2017 Kofax. Use of this code is with permission pursuant to Kofax license terms.
 *
 */
package com.fab.kofax.common

object JSONKeys {
    /***************************Profile keys */ // Profile Meta data
    const val NAME = "name"
    // Settings
    const val SETTINGS = "settings"

    // Camera Settings
    const val CAMERA_SETTINGS = "camerasettings"
    const val CAPTURE_EXPERIENCEMODE = "captureexperiencemode"
    const val ORIENTATION = "orientation"
    const val OFFSET_THRESHOLD = "offsetthreshold"
    const val STABILITYDELAY = "stabilitydelay"
    const val PITCH_THRESHOLD = "pitchthreshold"
    const val ROLL_THRESHOLD = "rollthreshold"
    const val MANUAL_CAPTURE_TIMER = "manualcapturetimer"
    const val CAPTURE_MODE = "capturetype"
    const val DETECTOR = "edgedetection"
    const val SHOW_GALLERY = "showgallery"
    const val AUTO_TORCH = "autotorch"
    const val SHOW_CAPTURE_DEMO = "showcapturedemo"
    const val SHOW_PAGE_DETECTION = "showpagedetection"
    const val SHOW_CAPTURE_DIAGNOSTICS = "showcapturediagnostics"
    const val ENABLE_GLARE_DETECTION = "enableGlareDetection"
    const val ENABLE_GLARE_EXPERIENCE = "enableGlareExperience"
    const val TILT_ANGLE = "tiltAngle"

    // EVRS Settings
    const val EVRS_SETTINGS = "evrssettings"
    const val USE_BANKRIGHT_SETTINGS = "usebankrightsettings"
    const val DO_QUICKANALYSIS = "doquickanalysis"
    const val IP_String = "ipstring"
    const val CheckBackIPString = "checkbackipstring"
    const val PROCESS_TYPE = "processtype"
    const val SEND_IMAGE_SUMMARY = "sendimagesummary"

    // Advanced Settings
    const val ADVANCED_SETTINGS = "advancedsettings"
    const val SEARCH_MICR = "searchmicr"
    const val USE_HAND_PRINT = "usehandprint"
    const val CHECK_FOR_DUPLICATES = "checkforduplicates"
    const val CHECK_VALIDATION_AT_SERVER = "checkvalidationatserver"
    const val SHOW_CHECK_INFO = "showcheckinfo"
    const val CHECK_EXTRACTION = "checkextraction"

    // Screen Keys
    const val SCREEN_TEXTS = "screentexts"
    const val SUMMARY = "summary"

    // Summary/Instruction Screen
    const val INSTRUCTION_TEXT = "instructiontext"
    const val IS_INSTRUCTION_TEXT_CHANGED = "isinstructiontextchanged"
    const val INSTRUCTION_TEXT_FOR_BACK = "instructiontextforback"
    const val IS_INSTRUCTION_TEXT_FOR_BACK_CHANGED = "isinstructiontextforbackchanged"
    const val SUBMIT_BUTTON_TEXT = "submitbuttontext"
    const val IS_SUBMIT_BUTTON_TEXT_CHANGED = "issubmitbuttontextchanged"
    const val SUBMIT_ALERT_TEXT = "submitalerttext"
    const val IS_SUBMIT_ALERT_TEXT_CHANGED = "issubmitalerttextchanged"
    const val SUBMIT_CANCEL_ALERT_TEXT = "submitcancelalerttext"
    const val IS_SUBMIT_CANCEL_ALERT_TEXT_CHANGED = "issubmitcancelalerttextchanged"
    const val CAMERA = "camera"

    // Camera screen
    const val UE_USER_INSTRUCTION_FRONT = "ueuserinstructionfront"
    const val IS_UE_USER_INSTRUCTION_FRONT_CHANGED = "isueuserinstructionfrontchanged"
    const val UE_MOVE_CLOSER = "uemovecloser"
    const val IS_UE_MOVE_CLOSER_CHANGED = "isuemovecloserchanged"
    const val UE_MOVE_BACK = "uemoveback"
    const val IS_UE_MOVE_BACK_CHANGED = "isuemovebackchanged"
    const val UE_HOLD_STEADY = "ueholdsteady"
    const val IS_UE_HOLD_STEADY_CHANGED = "isueholdsteadychanged"
    const val UE_USER_INSTRUCTION_BACK = "ueuserinstructionback"
    const val IS_UE_USER_INSTRUCTION_BACK_CHANGED = "isueuserinstructionbackchanged"
    const val UE_CENTER = "uecenter"
    const val IS_UE_CENTER_CHANGED = "isuecenterchanged"
    const val UE_CAPTURED = "uecaptured"
    const val IS_UE_CAPTURED_CHANGED = "isuecapturedchanged"
    const val UE_HOLD_PARALLEL_TEXT = "ueholdparallel"
    const val IS_UE_HOLD_PARALLEL_TEXT_CHANGED = "isueholdparallelchanged"
    const val UE_ORIENTATION_TEXT = "ueorientation"
    const val IS_UE_ORIENTATION_TEXT_CHANGED = "isueorientationchanged"
    const val UE_TILT_DEVICE_UP_TEXT = "ueTiltDeviceUp"
    const val IS_UE_TILT_DEVICE_UP_TEXT_CHANGED = "isuetiltdeviceuptextchanged"
    const val UE_TILT_DEVICE_FORWARD_TEXT = "ueTiltDeviceForward"
    const val IS_UE_TILT_DEVICE_FORWARD_TEXT_CHANGED = "isuetiltdeviceforwardtextchanged"
    const val PREVIEW = "preview"

    // Preview Screen
    const val FRONT_RETAKE_BUTTON = "frontretakebutton"
    const val IS_FRONT_RETAKE_BUTTON_CHANGED = "isfrontretakebuttonchanged"
    const val FRONT_USE_BUTTON = "frontusebutton"
    const val IS_FRONT_USE_BUTTON_CHANGED = "isfrontusebuttonchanged"
    const val BACK_USE_BUTTON = "backusebutton"
    const val IS_BACK_USE_BUTTON_CHANGED = "isbackusebuttonchanged"
    const val BACK_RETAKE_BUTTON = "backretakebutton"
    const val IS_BACK_RETAKE_BUTTON_CHANGED = "isbackretakebuttonchanged"
    const val CANCEL_BUTTON = "cancelbutton"
    const val IS_CANCEL_BUTTON_CHANGED = "iscancelbuttonchanged"

    /***************************profile keys */
    const val IS_NAME_CHANGED = "isnamechanged"
    const val TYPE = "type"
    const val COMPONENT_GRAPHICS = "componentgraphics"
    const val SUBMIT_TO = "submitto"
    const val ASPECT_RATIO = "frameaspectratio"
    const val TARGET_FRAME_PADDING = "targetframepadding"
    const val MAX_FILL_FRACTION = "maxfillfraction"
    const val MIN_FILL_FRACTION = "minfillfraction"
    const val TOLERANCE_FRACTION = "tolerancefraction"
    const val MAX_SKEW_ANGLE = "maxskewangle"
    const val IS_FIRST_TIME_CHECK_DEMO = "isfristtimecheckdemo"
    const val NUMBER_OF_DOCUMENTS_TO_CAPTURE = "numberofdocumentstocapture"

}