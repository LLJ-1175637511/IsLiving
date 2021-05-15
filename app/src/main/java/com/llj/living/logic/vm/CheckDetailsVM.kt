package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.llj.living.data.bean.InfoByEntIdBean

class CheckDetailsVM(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    private val _photoInfoLiveData = MutableLiveData<Boolean>()
    val photoInfo: LiveData<Boolean> = _photoInfoLiveData

    private val _recognizeLiveData = MutableLiveData<InfoByEntIdBean>()
    val recognizeLiveData: LiveData<InfoByEntIdBean> = _recognizeLiveData

    fun setPhotoInfo(bool: Boolean) {
        _photoInfoLiveData.postValue(bool)
    }



}