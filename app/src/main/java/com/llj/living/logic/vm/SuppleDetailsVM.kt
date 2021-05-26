package com.llj.living.logic.vm

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.llj.living.custom.exception.*
import com.llj.living.custom.ext.getSP
import com.llj.living.data.const.Const
import com.llj.living.data.enums.IdCardEnum
import com.llj.living.data.enums.ImageType
import com.llj.living.net.config.BaiduNetConfig
import com.llj.living.net.config.SysNetConfig
import com.llj.living.net.repository.BaiduRepository
import com.llj.living.net.repository.SystemRepository
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
    private var idCardABase64Str: String? = null
    private var idCardBBase64Str: String? = null

    suspend fun checkIDCardAFront() = viewModelScope.async(Dispatchers.Default) {
        idCardABase64Str ?: throw IdcardFErrException()
        val baiduToken = getSP(Const.SPBaidu).getString(Const.SPBaiduTokenString, "")
        baiduToken ?: throw BaiduTokenErrException()
        val baiduMap = BaiduNetConfig.buildIdCardRecognizeMap(
            idCardABase64Str!!,
            IdCardEnum.front
        )
        BaiduRepository.sendIdcardRecognizeRequest(baiduToken, baiduMap)
    }

    suspend fun checkIDCardBack() = viewModelScope.async(Dispatchers.Default) {
        idCardBBase64Str ?: throw IdcardBErrException()
        val baiduToken = getSP(Const.SPBaidu).getString(Const.SPBaiduTokenString, "")
        baiduToken ?: throw BaiduTokenErrException()
        val baiduMap = BaiduNetConfig.buildIdCardRecognizeMap(
            idCardBBase64Str!!,
            IdCardEnum.back
        )
        BaiduRepository.sendIdcardRecognizeRequest(baiduToken, baiduMap)
    }

    suspend fun uploadBaiduInfo(idNumber: String, peopleId: Int) =
        withContext(Dispatchers.Default) {
            val entId = getSP(Const.SPUser).getInt(Const.SPUserEntId, -1)
            base64Str ?: throw FaceLackException()
            if (entId == -1) throw EntIdException()
            val baiduToken = getSP(Const.SPBaidu).getString(Const.SPBaiduTokenString, "")
            baiduToken ?: throw BaiduTokenErrException()
            val baiduPeopleId = "${peopleId}_${idNumber.trim()}"
            LogUtils.d("${TAG}_TT", baiduPeopleId)
            val baiduMap = BaiduNetConfig.buildRegisterFaceMap(
                base64Str!!,
                ImageType.BASE64,
                entId.toString(),
                baiduPeopleId
            )
            BaiduRepository.sendRegisterFaceRequest(baiduToken, baiduMap)
        }

    @RequiresApi(Build.VERSION_CODES.R)
    suspend fun uploadPictureInfo(reputId: Int, peopleId: Int, proportion: Float?) =
        withContext(Dispatchers.Default) {
            val token = getSP(Const.SPUser).getString(Const.SPUserTokenLogin, "")
            token ?: throw TokenErrException()
            val map =
                SysNetConfig.buildUploadSuppleMap(token, reputId.toString(), peopleId.toString())
            val fileList = SysNetConfig.buildUploadSuppleFileMap(
                getApplication(), proportion
            )
            SystemRepository.getEntUploadPictureInfoRequest(map, fileList)
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

    fun setIdCardABase64Str(str: String?) {
        str?.let {
            idCardABase64Str = it
        }
    }

    fun setIdCardBBase64Str(str: String?) {
        str?.let {
            idCardBBase64Str = it
        }
    }

}