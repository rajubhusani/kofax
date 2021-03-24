package com.fab.kofax.component

import com.fab.kofax.common.JSONKeys
import org.json.JSONException
import org.json.JSONObject

abstract class Component {
    var name: String? = null
    private var isNameChnaged: Boolean? = false
    var type: String? = null
    var submitTo: String? = null
    @JvmField
    var settings: Settings? = null
    var localization: Localization? = null
    abstract fun deserialize(jsonComponent: JSONObject?): Any?
    abstract fun serialize(isForExport: Boolean): JSONObject?
    protected fun convertFromJSON(jsonComponent: JSONObject?) {
        settings = Settings()
        name = jsonComponent?.optString(JSONKeys.NAME)
        isNameChnaged = jsonComponent?.optBoolean(JSONKeys.IS_NAME_CHANGED)
        settings!!.convertFromJson(jsonComponent)
        type = jsonComponent?.optString(JSONKeys.TYPE)
        submitTo = jsonComponent?.optString(JSONKeys.SUBMIT_TO)
    }

    protected fun convertToJSONObject(isForExport: Boolean?): JSONObject {
        val jsonComponent = JSONObject()
        val jsonSettings = settings!!.convertToJSONObject()
        try {
            jsonComponent.put(JSONKeys.NAME, name)
            if (!isForExport!!) {
                jsonComponent.put(JSONKeys.IS_NAME_CHANGED, isNameChnaged)
            }
            jsonComponent.put(JSONKeys.TYPE, type)
            jsonComponent.put(JSONKeys.SUBMIT_TO, submitTo)
            jsonComponent.put(JSONKeys.SETTINGS, jsonSettings)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonComponent
    }
}