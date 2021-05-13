package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.llj.living.data.enums.IsShowedType

class SuppleInfoVM(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    private val _ivFaceCoverIsShowLiveData = MutableLiveData<Boolean>()
    val ivFaceCoverIsShowLiveData: LiveData<Boolean> = _ivFaceCoverIsShowLiveData

    private val _ivIdCardACoverIsShowLiveData = MutableLiveData<Boolean>()
    val ivIdCardACoverIsShowLiveData: LiveData<Boolean> = _ivIdCardACoverIsShowLiveData

    private val _ivIdCardBCoverIsShowLiveData = MutableLiveData<Boolean>()
    val ivIdCardBCoverIsShowLiveData: LiveData<Boolean> = _ivIdCardBCoverIsShowLiveData

    fun setPictureShow(){
        _ivFaceCoverIsShowLiveData.postValue(true)
    }

    fun setPictureAShow(){
        _ivIdCardACoverIsShowLiveData.postValue(true)
    }

    fun setPictureBShow(){
        _ivIdCardBCoverIsShowLiveData.postValue(true)
    }

    companion object {
        private const val TakePhotosCompleted = "TakePhotosCompleted"
    }
}