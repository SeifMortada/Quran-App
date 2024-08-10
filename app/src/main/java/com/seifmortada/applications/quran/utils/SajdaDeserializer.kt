package com.seifmortada.applications.quran.utils

import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonParseException
import com.seifmortada.applications.quran.domain.model.response.quran.SajdaObject
import java.lang.reflect.Type

class SajdaDeserializer : JsonDeserializer<Any> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Any {
        return if (json.isJsonObject) {
            // Deserialize as SajdaObject
            context.deserialize(json, SajdaObject::class.java)
        } else if (json.isJsonPrimitive) {
            // Deserialize as Boolean
            json.asBoolean
        } else {
            throw JsonParseException("Unknown type for sajda")
        }
    }
}