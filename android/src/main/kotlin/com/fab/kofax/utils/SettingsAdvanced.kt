package com.fab.kofax.utils

class SettingsAdvanced {
    // Check Deposit Advanced Settings
	@JvmField
	var SearchMICR = false
    @JvmField
	var UseHandPrint = false
    @JvmField
	var CheckForDuplicates = false
    @JvmField
	var CheckValidationAtServer = false
    @JvmField
	var ShowCheckInfo = false
    @JvmField
	var ShowCaptureDemo = false
    @JvmField
	var CheckExtraction = 0

    // Custom Component Advanced Settings
	@JvmField
	var NumberOfDocumentsToCapture = 0
    var NumberOfDocumentsToSubmit = 0
    var SubmitToKFS = false
    var SubmitToNormalServer = false
    var KFSUrl: String? = null
    var SubmitServerUrl: String? = null
    @JvmField
	var isFirstTimeCheckDemo = false
}