package com.llj.living.logic.vm

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.llj.living.custom.exception.BaiduTokenErrException
import com.llj.living.custom.exception.EntIdException
import com.llj.living.custom.exception.FaceLackException
import com.llj.living.custom.exception.TokenErrException
import com.llj.living.custom.ext.getSP
import com.llj.living.data.const.Const
import com.llj.living.data.enums.ImageType
import com.llj.living.net.config.BaiduNetConfig
import com.llj.living.net.config.SysNetConfig
import com.llj.living.net.repository.BaiduRepository
import com.llj.living.net.repository.SystemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SuppleDetailsVM(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    private val _ivFaceCoverIsShowLiveData = MutableLiveData<Boolean>()
    val ivFaceCoverIsShowLiveData: LiveData<Boolean> = _ivFaceCoverIsShowLiveData

    private val _ivIdCardACoverIsShowLiveData = MutableLiveData<Boolean>()
    val ivIdCardACoverIsShowLiveData: LiveData<Boolean> = _ivIdCardACoverIsShowLiveData

    private val _ivIdCardBCoverIsShowLiveData = MutableLiveData<Boolean>()
    val ivIdCardBCoverIsShowLiveData: LiveData<Boolean> = _ivIdCardBCoverIsShowLiveData

    private var base64Str: String? = null

    @RequiresApi(Build.VERSION_CODES.R)
    suspend fun uploadPictureInfo(reputId: Int, peopleId: Int, proportion: Float?) = withContext(Dispatchers.Default)  {
        val token = getSP(Const.SPUser).getString(Const.SPUserTokenLogin, "")
        base64Str ?: throw FaceLackException()
        token ?: throw TokenErrException()
        val map = SysNetConfig.buildUploadSuppleMap(token, reputId.toString(), peopleId.toString())
        val fileList = SysNetConfig.buildUploadSuppleFileMap(
            getApplication(), proportion
        )
        SystemRepository.getEntUploadPictureInfoRequest(map, fileList)
    }

    suspend fun uploadBaiduInfo(idNumber: String) = withContext(Dispatchers.Default) {
        val entId = getSP(Const.SPUser).getInt(Const.SPUserEntId, -1)
        if (entId == -1) throw EntIdException()
        val baiduToken = getSP(Const.SPBaidu).getString(Const.SPBaiduTokenString, "")
        baiduToken ?: throw BaiduTokenErrException()
        val baiduPeopleId = "${entId}_${idNumber}"
        val baiduMap = BaiduNetConfig.buildRegisterFaceMap(
            base64Str!!,
            ImageType.BASE64,
            entId.toString(),
            baiduPeopleId
        )
        BaiduRepository.sendRegisterFaceRequest(baiduToken, baiduMap)
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

    fun setBase64Str(str: String?) {
        str?.let {
            base64Str = it
        }
    }

}