package com.llj.living.application

import android.app.Application
import com.llj.living.utils.ContextUtils

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextUtils.init(applicationContext)
    }

    companion object {
        const val ApiKey = "f05CbSUbz4fpk5EFMfwNzgdh"
        const val SecretKey = "jFRKm4I4RAZ7YS7svK3xOQkuhfTcrf4e"

        const val CURRENT_VERSION = "1.0.1"

        //养老院位置 lon lat
        private var entLocation = Pair(0.0, 0.0)
        fun getEntLocation() = entLocation
        fun setEntLocation(locPair: Pair<Double, Double>) {
            entLocation = locPair
        }

        //当前设备位置 lon lat
        private var location = Pair(0.0, 0.0)
        fun getLocation() = location
        fun setLocation(locPair: Pair<Double, Double>) {
            location = locPair
        }
    }

}