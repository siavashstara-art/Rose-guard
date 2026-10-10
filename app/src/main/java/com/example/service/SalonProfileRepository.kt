package com.example.service

import android.content.Context
import android.content.SharedPreferences
import com.example.model.SalonProfile
import com.example.model.SalonTemplateLayout
import org.json.JSONArray
import org.json.JSONObject

class SalonProfileRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("tavana_salon_profiles_store", Context.MODE_PRIVATE)

    companion object {
        val DEFAULT_PROFILE_1 = SalonProfile(
            id = "salon_001",
            salonName = "سالن زیبایی نگین رویال",
            managerName = "سرکار خانم الهام کریمی",
            address = "تهران، سعادت‌آباد، میدان کاج، مجتمع تجاری سرو، طبقه ۳",
            phone = "۰۲۱-۲۲۰۶۸۵۴۱ / ۰۹۱۲۳۴۵۶۷۸۹",
            workingHours = "شنبه تا پنجشنبه ۱۰:۰۰ الی ۱۹:۳۰ (پذیرش با رزرو نوبت)",
            mainPhotoUri = null,
            bannerPhotoUri = null,
            logoPhotoUri = null,
            brandPrimaryColorHex = 0xFFE26A50,
            selectedTemplate = SalonTemplateLayout.TOP_HERO,
            isCustomApprovedByOwner = true
        )

        val DEFAULT_PROFILE_2 = SalonProfile(
            id = "salon_002",
            salonName = "سالن تخصصی عروس گل سرخ",
            managerName = "سرکار خانم مریم صادقی",
            address = "تهران، فرمانیه، خیابان جهانبخش‌نژاد، برج آرام، پلاک ۲۸",
            phone = "۰۲۱-۲۶۱۱۴۲۵۰ / ۰۹۱۹۸۷۶۵۴۳۲",
            workingHours = "همه‌روزه ۹:۳۰ الی ۲۱:۰۰ (خدمات VIP عروس و مزون)",
            mainPhotoUri = null,
            bannerPhotoUri = null,
            logoPhotoUri = null,
            brandPrimaryColorHex = 0xFFD65874,
            selectedTemplate = SalonTemplateLayout.HEADER_CARD,
            isCustomApprovedByOwner = true
        )
    }

    fun getAllProfiles(): List<SalonProfile> {
        val jsonStr = prefs.getString("saved_profiles_list", null)
        if (jsonStr.isNullOrBlank()) {
            val defaults = listOf(DEFAULT_PROFILE_1, DEFAULT_PROFILE_2)
            saveAllProfiles(defaults)
            return defaults
        }

        return try {
            val jsonArray = JSONArray(jsonStr)
            val list = mutableListOf<SalonProfile>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(deserializeProfile(obj))
            }
            if (list.isEmpty()) listOf(DEFAULT_PROFILE_1, DEFAULT_PROFILE_2) else list
        } catch (_: Exception) {
            listOf(DEFAULT_PROFILE_1, DEFAULT_PROFILE_2)
        }
    }

    fun saveProfile(profile: SalonProfile) {
        val all = getAllProfiles().toMutableList()
        val index = all.indexOfFirst { it.id == profile.id }
        if (index != -1) {
            all[index] = profile
        } else {
            all.add(profile)
        }
        saveAllProfiles(all)
    }

    fun getActiveProfileId(): String {
        return prefs.getString("active_profile_id", "salon_001") ?: "salon_001"
    }

    fun setActiveProfileId(id: String) {
        prefs.edit().putString("active_profile_id", id).apply()
    }

    private fun saveAllProfiles(profiles: List<SalonProfile>) {
        val array = JSONArray()
        profiles.forEach { profile ->
            array.put(serializeProfile(profile))
        }
        prefs.edit().putString("saved_profiles_list", array.toString()).apply()
    }

    private fun serializeProfile(p: SalonProfile): JSONObject {
        return JSONObject().apply {
            put("id", p.id)
            put("salonName", p.salonName)
            put("managerName", p.managerName)
            put("address", p.address)
            put("phone", p.phone)
            put("workingHours", p.workingHours)
            put("mainPhotoUri", p.mainPhotoUri ?: "")
            put("bannerPhotoUri", p.bannerPhotoUri ?: "")
            put("logoPhotoUri", p.logoPhotoUri ?: "")
            put("brandPrimaryColorHex", p.brandPrimaryColorHex)
            put("selectedTemplate", p.selectedTemplate.name)
            put("isCustomApprovedByOwner", p.isCustomApprovedByOwner)
        }
    }

    private fun deserializeProfile(obj: JSONObject): SalonProfile {
        val templateName = obj.optString("selectedTemplate", SalonTemplateLayout.TOP_HERO.name)
        val template = try {
            SalonTemplateLayout.valueOf(templateName)
        } catch (_: Exception) {
            SalonTemplateLayout.TOP_HERO
        }

        return SalonProfile(
            id = obj.getString("id"),
            salonName = obj.optString("salonName", "سالن زیبایی"),
            managerName = obj.optString("managerName", ""),
            address = obj.optString("address", ""),
            phone = obj.optString("phone", ""),
            workingHours = obj.optString("workingHours", ""),
            mainPhotoUri = obj.optString("mainPhotoUri").takeIf { it.isNotBlank() },
            bannerPhotoUri = obj.optString("bannerPhotoUri").takeIf { it.isNotBlank() },
            logoPhotoUri = obj.optString("logoPhotoUri").takeIf { it.isNotBlank() },
            brandPrimaryColorHex = obj.optLong("brandPrimaryColorHex", 0xFFE26A50),
            selectedTemplate = template,
            isCustomApprovedByOwner = obj.optBoolean("isCustomApprovedByOwner", true)
        )
    }
}
