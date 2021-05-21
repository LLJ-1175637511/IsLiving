package com.llj.living.application

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.llj.living.utils.ContextUtils

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextUtils.init(applicationContext)
    }

    companion object {

        const val CURRENT_VERSION = "1.1.1"

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

        private var distance = 0
        fun setDistance(d: Int) {
            distance = d
        }

        fun getDistance() = distance

        private val _tokenInvalidLiveData = MutableLiveData<Boolean>()
        val tokenInvalidLiveData: LiveData<Boolean> = _tokenInvalidLiveData
        fun setTokenInvalid(boolean: Boolean) {
            _tokenInvalidLiveData.postValue(boolean)
        }
    }

}