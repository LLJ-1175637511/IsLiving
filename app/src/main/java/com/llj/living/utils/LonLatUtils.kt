package com.llj.living.utils

import kotlin.math.*

object LonLatUtils {

    private const val EARTH_RADIUS = 6378137.0 //赤道半径

    private const val RAD = Math.PI / 180.0

    fun getDistance(
        entPair: Pair<Double, Double>,
        pair: Pair<Double, Double>
    ): Double {
        val lngEnt = entPair.first
        val latEnt = entPair.second
        val lng = pair.first
        val lat = pair.second
        val radLat1: Double = latEnt * RAD
        val radLat2: Double = lat * RAD
        val a = radLat1 - radLat2
        val b: Double = (lngEnt - lng) * RAD
        var s = 2 * asin(
            sqrt(
                sin(a / 2).pow(2.0) +
                        cos(radLat1) * cos(radLat2) * sin(b / 2).pow(2.0)
            )
        )
        s *= EARTH_RADIUS
        s = (s * 10000).roundToInt() / 10000.toDouble()
        return s
    }

}