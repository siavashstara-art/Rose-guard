package com.example.model

import android.content.Context
import android.net.Uri
import com.example.R

/**
 * Robust image resolver for Salon Tour assets and user-uploaded media.
 * Completely avoids issues with reflection or applicationId vs namespace mismatches
 * by providing direct static references to bundled drawables.
 */
object SalonTourImageResolver {

    /**
     * Resolves any asset identifier or URI to a valid Coil-supported model:
     * - Direct Int resource ID for bundled drawables (R.drawable.img_tour_...)
     * - Uri or String for local content/file URIs or network URLs
     */
    fun resolveImageModel(context: Context, uriOrName: String?): Any? {
        if (uriOrName.isNullOrBlank()) return null

        val clean = uriOrName.substringAfterLast("/").substringBeforeLast(".")

        // 1. Direct compile-time mapping to bundled high-resolution assets
        val directDrawable = when (clean) {
            "img_tour_reception" -> R.drawable.img_tour_reception
            "img_tour_bridal" -> R.drawable.img_tour_bridal
            "img_tour_hair" -> R.drawable.img_tour_hair
            "img_tour_spa" -> R.drawable.img_tour_spa
            "img_tour_nail" -> R.drawable.img_tour_nail
            "tavana_icon" -> R.drawable.tavana_icon
            else -> null
        }
        if (directDrawable != null) {
            return directDrawable
        }

        // 2. Safe fallback identifier lookup in namespace
        try {
            val resIdNamespace = context.resources.getIdentifier(clean, "drawable", "com.example")
            if (resIdNamespace != 0) return resIdNamespace

            val resIdPackage = context.resources.getIdentifier(clean, "drawable", context.packageName)
            if (resIdPackage != 0) return resIdPackage
        } catch (_: Exception) {
        }

        // 3. User device content URI or file path
        if (uriOrName.startsWith("content://") || uriOrName.startsWith("file://") || uriOrName.startsWith("http://") || uriOrName.startsWith("https://")) {
            return Uri.parse(uriOrName)
        }

        return uriOrName
    }

    /**
     * Returns the direct drawable resource ID for a known station ID or null.
     */
    fun getDrawableForStation(stationId: String): Int {
        return when (stationId) {
            "tour_reception" -> R.drawable.img_tour_reception
            "tour_bridal" -> R.drawable.img_tour_bridal
            "tour_hair" -> R.drawable.img_tour_hair
            "tour_spa" -> R.drawable.img_tour_spa
            "tour_nail" -> R.drawable.img_tour_nail
            else -> R.drawable.img_tour_reception
        }
    }
}
