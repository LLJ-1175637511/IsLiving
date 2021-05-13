package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.llj.living.data.enums.IsShowedType

class SuppleInfoVM(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    private val _ivFaceCoverIsShowLD = getLiveDataForKey<Boolean>(IsShowedType.Face.name)

    private val _ivFaceCoverIsShowLiveData = MutableLiveData<Boolean>()
    val ivFaceCoverIsShowLiveData: LiveData<Boolean> = _ivFaceCoverIsShowLiveData

    private val _ivIdCardACoverIsShowLD = getLiveDataForKey<Boolean>(IsShowedType.IdCardA.name)

    private val _ivIdCardACoverIsShowLiveData = MutableLiveData<Boolean>()
    val ivIdCardACoverIsShowLiveData: LiveData<Boolean> = _ivIdCardACoverIsShowLiveData

    private val _ivIdCardBCoverIsShowLD = getLiveDataForKey<Boolean>(IsShowedType.IdCardB.name)

    private val _ivIdCardBCoverIsShowLiveData = MutableLiveData<Boolean>()
    val ivIdCardBCoverIsShowLiveData: LiveData<Boolean> = _ivIdCardBCoverIsShowLiveData


    fun setPictureAShow(){

    }

    fun setPictureBShow(){

    }

    fun setPictureCShow(){

    }

    fun getIsHadTakePhotoLiveData(key: IsShowedType): LiveData<Boolean> = when (key) {
        IsShowedType.Face -> _ivFaceCoverIsShowLD
        IsShowedType.IdCardA -> _ivIdCardACoverIsShowLD
        IsShowedType.IdCardB -> _ivIdCardBCoverIsShowLD
    }

    fun setShowedUi(key: IsShowedType) {
        val tempLiveData = when (key) {
            IsShowedType.Face -> _ivFaceCoverIsShowLD
            IsShowedType.IdCardA -> _ivIdCardACoverIsShowLD
            IsShowedType.IdCardB -> _ivIdCardBCoverIsShowLD
        }
        tempLiveData.postValue(true)
    }

    companion object {
        private const val TakePhotosCompleted = "TakePhotosCompleted"
    }
}