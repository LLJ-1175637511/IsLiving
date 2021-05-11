package com.llj.living.application

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.llj.living.data.bean.MainFragmentBean
import com.llj.living.data.bean.SecondFragmentBean
import com.llj.living.utils.ContextUtils

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextUtils.init(applicationContext)
    }

    companion object {
        const val ApiKey = "f05CbSUbz4fpk5EFMfwNzgdh"
        const val SecretKey = "jFRKm4I4RAZ7YS7svK3xOQkuhfTcrf4e"
//        const val ApiKey = "ySnmVuCazORiUEc5LcqPDhLn"
//        const val SecretKey = "jiNAFnIgBXAw7m89Zc6di1lsGPTOufy9"

        private val _hadFinishedList = MutableLiveData<MutableList<MainFragmentBean>>()
        val hadFinishedList: LiveData<MutableList<MainFragmentBean>> = _hadFinishedList
        fun setHadFinishedList(list: MutableList<MainFragmentBean>) {
            _hadFinishedList.postValue(list)
        }

        private val _suppleDoingList = MutableLiveData<MutableList<MainFragmentBean>>()
        val suppleDoingList: LiveData<MutableList<MainFragmentBean>> = _suppleDoingList
        fun setSuppleDoingList(list: MutableList<MainFragmentBean>) {
            _suppleDoingList.postValue(list)
        }

        private val _suppleWaitList = MutableLiveData<MutableList<SecondFragmentBean>>()
        val suppleWaitList: LiveData<MutableList<SecondFragmentBean>> = _suppleWaitList
        fun setSuppleWaitList(list: MutableList<SecondFragmentBean>) {
            _suppleWaitList.postValue(list)
        }

        private val _suppleHadList = MutableLiveData<MutableList<SecondFragmentBean>>()
        val suppleHadList: LiveData<MutableList<SecondFragmentBean>> = _suppleHadList
        fun setSuppleHadList(list: MutableList<SecondFragmentBean>) {
            _suppleHadList.postValue(list)
        }

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