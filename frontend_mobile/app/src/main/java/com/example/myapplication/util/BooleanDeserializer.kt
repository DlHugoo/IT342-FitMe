package com.example.myapplication.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class BooleanDeserializer : JsonDeserializer<Boolean> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Boolean {
        return when {
            json.isJsonPrimitive -> {
                val value = json.asJsonPrimitive
                if (value.isBoolean) {
                    value.asBoolean
                } else if (value.isNumber) {
                    value.asInt != 0 // Treat non-zero as true
                } else if (value.isString) {
                    value.asString.equals("true", ignoreCase = true) || value.asString == "1"
                } else {
                    false
                }
            }
            else -> false
        }
    }
}