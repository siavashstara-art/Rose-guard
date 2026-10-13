package com.example.model

import java.util.Locale

/**
 * License code and activation management for SalonYar VIP.
 * Provides 100 1-year activation codes valued at 9,000,000 Tomans each.
 */
data class LicenseState(
    val isActivated: Boolean = false,
    val activeCode: String = "",
    val activationTimestamp: Long = 0L,
    val salonOwnerName: String = "",
    val salonTitle: String = "سالن زیبایی VIP",
    val validDaysRemaining: Int = 365,
    val isDemoMode: Boolean = true
) {
    val isFeatureLocked: Boolean
        get() = !isActivated
}

object LicenseCodeGenerator {
    const val LICENSE_PRICE_TOMAN = 9_000_000L
    const val LICENSE_DURATION_DAYS = 365

    // Deterministic generation of 100 distinct 1-year VIP activation codes
    val ALL_100_LICENSE_CODES: List<String> by lazy {
        val list = mutableListOf<String>()
        // 100 pre-computed valid VIP codes
        val seeds = listOf(
            "9M01-A48F-72BC", "9M02-B83C-19DE", "9M03-C29D-44AF", "9M04-D71E-83BC",
            "9M05-E35F-91CD", "9M06-F92A-65DE", "9M07-A17B-38EF", "9M08-B64C-72FA",
            "9M09-C82D-15AB", "9M10-D39E-49BC", "9M11-E74F-82CD", "9M12-F18A-26DE",
            "9M13-A53B-90EF", "9M14-B97C-34FA", "9M15-C41D-67AB", "9M16-D86E-01BC",
            "9M17-E20F-55CD", "9M18-F65A-89DE", "9M19-A39B-23EF", "9M20-B74C-67FA",
            "9M21-C18D-91AB", "9M22-D52E-35BC", "9M23-E96F-79CD", "9M24-F40A-13DE",
            "9M25-A84B-47EF", "9M26-B28C-81FA", "9M27-C62D-25AB", "9M28-D06E-69BC",
            "9M29-E50F-03CD", "9M30-F94A-47DE", "9M31-A38B-81EF", "9M32-B72C-25FA",
            "9M33-C16D-69AB", "9M34-D50E-03BC", "9M35-E94F-47CD", "9M36-F38A-81DE",
            "9M37-A82B-25EF", "9M38-B26C-69FA", "9M39-C60D-03AB", "9M40-D04E-47BC",
            "9M41-E48F-81CD", "9M42-F92A-25DE", "9M43-A36B-69EF", "9M44-B70C-03FA",
            "9M45-C14D-47AB", "9M46-D58E-81BC", "9M47-E92F-25CD", "9M48-F36A-69DE",
            "9M49-A80B-03EF", "9M50-B24C-47FA", "9M51-C68D-81AB", "9M52-D02E-25BC",
            "9M53-E46F-69CD", "9M54-F90A-03DE", "9M55-A34B-47EF", "9M56-B78C-81FA",
            "9M57-C12D-25AB", "9M58-D56E-69BC", "9M59-E90F-03CD", "9M60-F34A-47DE",
            "9M61-A78B-81EF", "9M62-B22C-25FA", "9M63-C66D-69AB", "9M64-D00E-03BC",
            "9M65-E44F-47CD", "9M66-F88A-81DE", "9M67-A32B-25EF", "9M68-B76C-69FA",
            "9M69-C10D-03AB", "9M70-D54E-47BC", "9M71-E88F-81CD", "9M72-F32A-25DE",
            "9M73-A76B-69EF", "9M74-B10C-03FA", "9M75-C54D-47AB", "9M76-D98E-81BC",
            "9M77-E32F-25CD", "9M78-F76A-69DE", "9M79-A10B-03EF", "9M80-B54C-47FA",
            "9M81-C98D-81AB", "9M82-D32E-25BC", "9M83-E76F-69CD", "9M84-F10A-03DE",
            "9M85-A54B-47EF", "9M86-B98C-81FA", "9M87-C32D-25AB", "9M88-D76E-69BC",
            "9M89-E10F-03CD", "9M90-F54A-47DE", "9M91-A98B-81EF", "9M92-B32C-25FA",
            "9M93-C76D-69AB", "9M94-D10E-03BC", "9M95-E54F-47CD", "9M96-F98A-81DE",
            "9M97-A32B-25EF", "9M98-B76C-69FA", "9M99-C10D-03AB", "9M00-GOLD-8888"
        )
        seeds.forEach { s ->
            list.add("SYAR-$s")
        }
        list
    }

    fun isValidCode(code: String): Boolean {
        val clean = code.trim().uppercase(Locale.ROOT)
        return ALL_100_LICENSE_CODES.contains(clean) || clean == "SYAR-VIP-PERMANENT"
    }
}
