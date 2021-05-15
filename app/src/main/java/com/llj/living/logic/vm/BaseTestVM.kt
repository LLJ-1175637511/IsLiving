package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.llj.living.data.enums.NetStatus
import com.llj.living.data.enums.UpdateStatusType

open class BaseTestVM(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    private fun getNetStatusLiveData(type: UpdateStatusType): MutableLiveData<NetStatus> {
        val key = "${type.name}$NET_STATUS"
        if (!getSavedHandle().contains(key)) {
            getSavedHandle()[key] = NetStatus.INIT
        }
        return getSavedHandle().getLiveData<NetStatus>(key)
    }

    fun updateNetStatus(netStatus: NetStatus, type: UpdateStatusType) {
        getNetStatusLiveData(type).postValue(netStatus)
    }

    fun getNSLiveData(type: UpdateStatusType): LiveData<NetStatus> {
        return getNetStatusLiveData(type)
    }

    companion object {
        const val NET_STATUS = "net_status"
    }
}