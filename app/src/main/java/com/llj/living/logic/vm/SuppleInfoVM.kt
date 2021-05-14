package com.llj.living.logic.vm

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.llj.living.custom.exception.TokenErrException
import com.llj.living.custom.ext.getSP
import com.llj.living.data.const.Const
import com.llj.living.net.config.SysNetConfig
import com.llj.living.net.repository.SystemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SuppleInfoVM(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    private val _ivFaceCoverIsShowLiveData = MutableLiveData<Boolean>()
    val ivFaceCoverIsShowLiveData: LiveData<Boolean> = _ivFaceCoverIsShowLiveData

    private val _ivIdCardACoverIsShowLiveData = MutableLiveData<Boolean>()
    val ivIdCardACoverIsShowLiveData: LiveData<Boolean> = _ivIdCardACoverIsShowLiveData

    private val _ivIdCardBCoverIsShowLiveData = MutableLiveData<Boolean>()
    val ivIdCardBCoverIsShowLiveData: LiveData<Boolean> = _ivIdCardBCoverIsShowLiveData

    @RequiresApi(Build.VERSION_CODES.R)
    suspend fun uploadPictureInfo(reputId: Int, peopleId: Int, proportion: Float?) = withContext(Dispatchers.Default) {
        val token = getSP(Const.SPUser).getString(Const.SPUserTokenLogin, "")
        token ?: throw TokenErrException()
        val map = SysNetConfig.buildUploadSuppleMap(token,reputId.toString(), peopleId.toString())
        val fileList = SysNetConfig.buildUploadSuppleFileMap(
            getApplication(),proportion
        )
        SystemRepository.getEntUploadPictureInfoRequest(map,fileList)
    }

    fun setPictureShow() {
        _ivFaceCoverIsShowLiveData.postValue(true)
    }

    fun setPictureAShow() {
        _ivIdCardACoverIsShowLiveData.postValue(true)
    }

    fun setPictureBShow() {
        _ivIdCardBCoverIsShowLiveData.postValue(true)
    }

}