package com.example.data

import kotlin.math.*

object QiblaCalculator {
    const val MAKKAH_LATITUDE = 21.422487
    const val MAKKAH_LONGITUDE = 39.826206

    /**
     * Calculates the forward azimuth (bearing) from user coordinates to Holy Kaaba in degrees (0..360).
     */
    fun calculateBearing(userLat: Double, userLng: Double): Float {
        val phi1 = Math.toRadians(userLat)
        val phi2 = Math.toRadians(MAKKAH_LATITUDE)
        val deltaLambda = Math.toRadians(MAKKAH_LONGITUDE - userLng)

        val y = sin(deltaLambda) * cos(phi2)
        val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(deltaLambda)

        val bearingRad = atan2(y, x)
        val bearingDeg = Math.toDegrees(bearingRad)
        return ((bearingDeg + 360.0) % 360.0).toFloat()
    }

    /**
     * Calculates distance to Holy Kaaba in kilometers using Haversine formula.
     */
    fun calculateDistanceKm(userLat: Double, userLng: Double): Int {
        val earthRadiusKm = 6371.0
        val dLat = Math.toRadians(MAKKAH_LATITUDE - userLat)
        val dLng = Math.toRadians(MAKKAH_LONGITUDE - userLng)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(userLat)) * cos(Math.toRadians(MAKKAH_LATITUDE)) *
                sin(dLng / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return (earthRadiusKm * c).roundToInt()
    }
}
