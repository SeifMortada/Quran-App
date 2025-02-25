package com.seifmortada.applications.quran.core.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.domain.model.AzkarItemModel
import com.example.domain.model.AzkarModel
import com.example.domain.model.MoshafModel
import com.example.domain.model.ReciterModel
import com.example.domain.model.reciter_surah_moshaf.SurahMoshafReciter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromString

object CustomNavType {

    val reciterType = object : NavType<ReciterModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): ReciterModel? {
            return decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): ReciterModel {
            return decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: ReciterModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: ReciterModel) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }

    val tilawahType = object : NavType<MoshafModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): MoshafModel? {
            return decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): MoshafModel {
            return decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: MoshafModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: MoshafModel) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
    val azkarModel = object : NavType<AzkarModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): AzkarModel? {
            return decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): AzkarModel {
            return decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: AzkarModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: AzkarModel) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }

    val azkarItemModel = object : NavType<AzkarItemModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): AzkarItemModel? {
            return decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): AzkarItemModel {
            return decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: AzkarItemModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: AzkarItemModel) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
    val surahTelawahReciter = object : NavType<SurahMoshafReciter>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): SurahMoshafReciter? {
            return decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): SurahMoshafReciter {
            return decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: SurahMoshafReciter): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: SurahMoshafReciter) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}