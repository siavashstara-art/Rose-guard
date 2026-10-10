package com.example.service

import android.content.Context
import android.content.SharedPreferences
import com.example.model.LicenseStatus
import com.example.model.LicenseTier
import com.example.model.SalonLicense
import java.security.MessageDigest
import java.util.Locale

sealed interface LicenseValidationResult {
    data class Success(val license: SalonLicense) : LicenseValidationResult
    data class InvalidKey(val message: String) : LicenseValidationResult
    data class Expired(val message: String, val license: SalonLicense) : LicenseValidationResult
    data class Revoked(val message: String) : LicenseValidationResult
}

class LicenseValidationService(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("tavana_license_store", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ACTIVE_CODE = "active_license_code"
        private const val KEY_ACTIVE_TIER = "active_license_tier"
        private const val KEY_ACTIVE_INDEX = "active_license_index"

        // Salt used to deterministically generate non-guessable, cryptographic tokens for licenses 001..200
        private const val SYSTEM_MASTER_SALT = "TAVANA_STUDIO_2026_SALONYAR_SECURE_SALT"

        /**
         * Generates the non-guessable client secret key for a given license index (1..200).
         * Format: TAV-SALON-XXX-YYYY-ZZZZ
         */
        fun generateSecretKeyForIndex(index: Int): String {
            val formattedIndex = String.format(Locale.US, "%03d", index)
            val raw = "$SYSTEM_MASTER_SALT:$formattedIndex"
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(raw.toByteArray(Charsets.UTF_8))
            val hex = digest.joinToString("") { "%02x".format(it) }.uppercase(Locale.US)
            val part1 = hex.substring(0, 4)
            val part2 = hex.substring(4, 8)
            return "TAV-SALON-$formattedIndex-$part1-$part2"
        }
    }

    /**
     * Generates the 200 customer licenses registry for the management dashboard.
     */
    fun getAll200Licenses(): List<SalonLicense> {
        return (1..200).map { i ->
            val code = generateSecretKeyForIndex(i)
            val status = when {
                i == 13 -> LicenseStatus.REVOKED
                i == 42 -> LicenseStatus.EXPIRED
                i <= 25 -> LicenseStatus.ACTIVE
                i <= 80 -> LicenseStatus.ALLOCATED
                else -> LicenseStatus.UNISSUED
            }

            val allocatedSalon = when (i) {
                1 -> "سالن زیبایی نگین رویال"
                2 -> "سالن تخصصی گل سرخ"
                3 -> "سالن زیبایی ماتیک"
                13 -> "سالن لغوشده آریا"
                42 -> "سالن منقضی نیلوفر"
                else -> if (i <= 80) "سالن زیبایی همکار کد $i" else null
            }

            SalonLicense(
                licenseIndex = i,
                clientSecretKey = code,
                allocatedSalonId = if (i <= 80) "salon_$i" else null,
                allocatedSalonName = allocatedSalon,
                status = status,
                activationDate = if (i <= 25) "۱۴۰۳/۰۶/۱۵" else null,
                expirationDate = if (i == 42) "۱۴۰۳/۰۷/۰۱" else if (i <= 25) "۱۴۰۴/۰۶/۱۵" else null,
                tier = if (i <= 10) LicenseTier.ENTERPRISE_PRO else if (i <= 80) LicenseTier.COMMERCIAL_STANDARD else LicenseTier.FREE_PREVIEW,
                allowedFeatures = if (i <= 10) listOf("EXPORT_HD", "AI_ENHANCED", "PREMIUM_TEMPLATES") else listOf("EXPORT_HD")
            )
        }
    }

    fun getCurrentActiveLicense(): SalonLicense? {
        val code = prefs.getString(KEY_ACTIVE_CODE, null) ?: return null
        val index = prefs.getInt(KEY_ACTIVE_INDEX, 1)
        val tierName = prefs.getString(KEY_ACTIVE_TIER, LicenseTier.COMMERCIAL_STANDARD.name)
        val tier = try {
            LicenseTier.valueOf(tierName!!)
        } catch (_: Exception) {
            LicenseTier.COMMERCIAL_STANDARD
        }

        return SalonLicense(
            licenseIndex = index,
            clientSecretKey = code,
            status = LicenseStatus.ACTIVE,
            activationDate = "۱۴۰۳/۰۷/۰۱",
            expirationDate = "۱۴۰۴/۰۷/۰۱",
            tier = tier,
            allowedFeatures = listOf("EXPORT_HD", "PREMIUM_TEMPLATES", "AI_ENHANCED")
        )
    }

    fun isPremiumUnlocked(): Boolean {
        return getCurrentActiveLicense() != null
    }

    /**
     * Validates and activates a license key submitted by a client.
     */
    fun validateAndActivateKey(inputKey: String): LicenseValidationResult {
        val cleanKey = inputKey.trim().uppercase(Locale.US)

        // Parse key pattern TAV-SALON-XXX-YYYY-ZZZZ
        val parts = cleanKey.split("-")
        if (parts.size != 5 || parts[0] != "TAV" || parts[1] != "SALON") {
            return LicenseValidationResult.InvalidKey("قالب کد نامعتبر است. فرمت صحیح: TAV-SALON-001-XXXX-YYYY")
        }

        val indexStr = parts[2]
        val index = indexStr.toIntOrNull()
        if (index == null || index < 1 || index > 200) {
            return LicenseValidationResult.InvalidKey("شناسه مجوز باید بین ۰۰۱ تا ۲۰۰ باشد.")
        }

        val expectedKey = generateSecretKeyForIndex(index)
        if (cleanKey != expectedKey) {
            return LicenseValidationResult.InvalidKey("کد امنیتی با شناسه مجوز همخوانی ندارد یا کلید اشتباه است.")
        }

        // Check simulated status scenarios for testing requirement (active, expired, revoked)
        if (index == 13) {
            return LicenseValidationResult.Revoked("این مجوز توسط مدیریت مرکزی لغو شده است (کد تخلف ۱۳). لطفاً با پشتیبانی تماس بگیرید.")
        }
        if (index == 42) {
            val expiredLicense = SalonLicense(
                licenseIndex = 42,
                clientSecretKey = cleanKey,
                status = LicenseStatus.EXPIRED,
                activationDate = "۱۴۰۲/۰۶/۰۱",
                expirationDate = "۱۴۰۳/۰۶/۰۱",
                tier = LicenseTier.COMMERCIAL_STANDARD
            )
            return LicenseValidationResult.Expired("اعتبار این مجوز در تاریخ ۱۴۰۳/۰۶/۰۱ به پایان رسیده است. نیاز به تمدید سالانه دارد.", expiredLicense)
        }

        val activatedLicense = SalonLicense(
            licenseIndex = index,
            clientSecretKey = cleanKey,
            status = LicenseStatus.ACTIVE,
            activationDate = "۱۴۰۳/۰۷/۱۰",
            expirationDate = "۱۴۰۴/۰۷/۱۰",
            tier = if (index <= 10) LicenseTier.ENTERPRISE_PRO else LicenseTier.COMMERCIAL_STANDARD,
            allowedFeatures = listOf("EXPORT_HD", "PREMIUM_TEMPLATES", "AI_ENHANCED")
        )

        // Persist client activation
        prefs.edit()
            .putString(KEY_ACTIVE_CODE, cleanKey)
            .putInt(KEY_ACTIVE_INDEX, index)
            .putString(KEY_ACTIVE_TIER, activatedLicense.tier.name)
            .apply()

        return LicenseValidationResult.Success(activatedLicense)
    }

    fun deactivateLicense() {
        prefs.edit()
            .remove(KEY_ACTIVE_CODE)
            .remove(KEY_ACTIVE_TIER)
            .remove(KEY_ACTIVE_INDEX)
            .apply()
    }
}
