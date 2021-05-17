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

class CheckDetailsVM(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    private val _peopleInfoShowLiveData = MutableLiveData<Boolean>(false)
    val peopleInfoShowLiveData: LiveData<Boolean> = _peopleInfoShowLiveData

    private var base64Str: String? = null

    suspend fun verifyFaceRealness() = withContext(Dispatchers.Default) {
        val baiduToken = getSP(Const.SPBaidu).getString(Const.SPBaiduTokenString, "")
        baiduToken ?: throw BaiduTokenErrException()
        base64Str ?: throw FaceLackException()
        val baiduMap = BaiduNetConfig.buildFaceRealnessJson(
            base64Str!!,
            ImageType.BASE64
        )
        BaiduRepository.sendFaceRealnessRequest(baiduToken, baiduMap)
    }

    suspend fun searchBaiduInfo() = withContext(Dispatchers.Default) {
        val entId = getSP(Const.SPUser).getInt(Const.SPUserEntId, -1)
        if (entId == -1) throw EntIdException()
        val baiduToken = getSP(Const.SPBaidu).getString(Const.SPBaiduTokenString, "")
        baiduToken ?: throw BaiduTokenErrException()
        base64Str ?: throw FaceLackException()
        val baiduMap = BaiduNetConfig.buildSearchFaceMap(
            base64Str!!,
            ImageType.BASE64,
            entId.toString()
        )
        BaiduRepository.sendSearchRequest(baiduToken, baiduMap)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    suspend fun verifyIdNumber(
        checkId: Int,
        peopleId: Int,
        faceLivenessScore: Int,
        sameScore: Int,
        proportion: Float?
    ) = withContext(Dispatchers.Default) {
        val token = getSP(Const.SPUser).getString(Const.SPUserTokenLogin, "")
        token ?: throw TokenErrException()
        val map = SysNetConfig.buildVerifyCheckMap(
            token,
            checkId.toString(),
            peopleId.toString(),
            faceLivenessScore,
            sameScore
        )
        val checkPhotoFilePart = SysNetConfig.buildVerifyCheckFileMap(getApplication(),proportion)
        SystemRepository.getCheckSucRequest(map,checkPhotoFilePart)
    }

    fun setPhotoInfo(bool: Boolean) {
        _peopleInfoShowLiveData.postValue(bool)
    }

    fun setBase64Str(str: String?) {
        str?.let {
            base64Str = it
        }
    }

}