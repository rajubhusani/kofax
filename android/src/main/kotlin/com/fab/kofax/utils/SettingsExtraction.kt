package com.fab.kofax.utils

import com.kofax.kmc.kut.utilities.Licensing.LicenseServerType

class SettingsExtraction {
    var RTTIServerUrl: String? = null
    var KTAServerUrl: String? = null
    var AuthenticKTAServerUrl: String? = null
    var RTTIKofaxServerUrl: String? = null
    var KTAKofaxServerUrl: String? = null
    var ShowDefaultFields = false
    var FieldLabels: String? = null
    var Is_FieldLabels_changed = false
    var ConnectionTimeout = 0
    var SocketTimeout = 0
    var HighlightExtractedData = false
    var ShowHighlightsSwitch = false
    var SendOriginalImage = false
    var DeleteDocument = false
    var ODEAuthenticationDeleteDocument = false
    var KTASessionID: String? = null
    var AuthenticKTASessionID: String? = null
    var KTAKofaxSessionID: String? = null
    var KTAProcessName: String? = null
    var AuthenticKTAProcessName: String? = null
    var KTAKofaxProcessName: String? = null
    var KTAInputVariables: String? = null
    var KTAKofaxIDType: String? = null
    var ODELicenseRTTIServerUrl: String? = null
    var ODELicenseKTAServerUrl: String? = null
    var ODEAcquireCount = 0
    var ODELicenseServerType: LicenseServerType? = null
    var ODEModeldownloadUrl: String? = null
    var AuthenticRTTIServerUrl: String? = null
}