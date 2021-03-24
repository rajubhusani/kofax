package com.fab.kofax.component

import org.json.JSONObject

interface ILocalization {
    fun ConvertFromJson(jsonComponent: JSONObject?): Any?
    fun ConvertToJSONObject(isForExport: Boolean): JSONObject?
}