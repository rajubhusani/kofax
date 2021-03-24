package com.fab.kofax.component

import com.fab.kofax.CapturedImages
import com.fab.kofax.common.JSONKeys
import org.json.JSONException
import org.json.JSONObject

class CheckDepositComponent : Component() {

    override fun deserialize(jsonComponent: JSONObject?): Any? {
        convertFromJSON(jsonComponent)
        settings?.camerasettings?.AspectRatio = CapturedImages.FRONT_CHECK_ASPECT_RATIO
        return this
    }

    override fun serialize(isForExport: Boolean): JSONObject? {
        val jsonComponent = convertToJSONObject(isForExport)
        val jsonLocalization = localization?.ConvertToJSONObject(isForExport)
        if (isForExport && jsonComponent.optJSONObject(JSONKeys.SETTINGS).optJSONObject(JSONKeys.CAMERA_SETTINGS).has(JSONKeys.ASPECT_RATIO)) {
            jsonComponent.optJSONObject(JSONKeys.SETTINGS).optJSONObject(JSONKeys.CAMERA_SETTINGS).remove(JSONKeys.ASPECT_RATIO)
        }
        try {
            jsonComponent.put("screentexts", jsonLocalization)
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return jsonComponent
    }
}