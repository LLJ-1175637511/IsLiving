package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.llj.living.custom.ext.isMsgSuc
import com.llj.living.data.bean.MatchFaceData
import com.llj.living.data.const.Const
import com.llj.living.data.enums.ModifyFaceType
import com.llj.living.net.repository.FaceAuthRepository
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.launch

class FaceAuthViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    private val TAG = this.javaClass.simpleName

    fun getContentLiveData() = getLiveDataForKey<String>(Const.FaceAuthContent)

    fun getPhotoLiveData() = getLiveDataForKey<String>(Const.FaceAuthPhoto)

    fun getBaseFaceIdLiveData() = getLiveDataForKey<String>("registerFaceId")

    fun getPhotoIdLiveData() = getLiveDataForKey<String>(Const.FaceAuthPhotoId)

    fun modifyFace(
        map: Map<String, String>,
        type: ModifyFaceType
    ) = viewModelScope.launch {
        checkBaiduTokenRequest { token ->
            val result = FaceAuthRepository.sendModifyFaceRequest(token, map, type)
            val msg = if (result.isSuc) {
                if (type == ModifyFaceType.Register) getBaseFaceIdLiveData().postValue(result.data)
                getPhotoIdLiveData().postValue(result.data)
                "操作成功 唯一码：${result.data}"
            } else "操作失败 code:${result.data}"
            getContentLiveData().postValue(msg)
            LogUtils.d(TAG, msg)
        }
    }

    fun deleteFace(map: Map<String, String>) = viewModelScope.launch {
        checkBaiduTokenRequest { token ->
            val result = FaceAuthRepository.sendDeleteFaceRequest(token, map)
            val msg =
                if (result.error_code==0 && result.error_msg.isMsgSuc()) "删除成功 唯一码：${getPhotoIdLiveData().value}"
                else "删除失败 msg:${result.error_msg}"
            getContentLiveData().postValue(msg)
            LogUtils.d(TAG, msg)
        }
    }

    fun matchFace(mfbList: List<MatchFaceData>) = viewModelScope.launch {
        checkBaiduTokenRequest { token ->
            val result = FaceAuthRepository.sendMatchRequest(token, mfbList)
            if (result.isSuc) getContentLiveData().postValue("比对成功，相似率：${result.data}%")
            else getContentLiveData().postValue("比对失败，相似率：${result.data}%")
        }
    }

    fun searchFace(map: Map<String, String>) = viewModelScope.launch {
        checkBaiduTokenRequest { token ->
            val result = FaceAuthRepository.sendSearchRequest(token, map)
            if (result.isSuc) getContentLiveData().postValue("搜索成功，${result.data}")
            else getContentLiveData().postValue("比对失败，${result.data}")
        }
    }

    fun searchFaceInZn(map: Map<String, String>) = viewModelScope.launch {
        checkBaiduTokenRequest { token ->
            FaceAuthRepository.sendSearchInZnRequest(token, map)
           /* val result = FaceAuthRepository.sendSearchInZnRequest(token, map)
            if (result.isSuc) getContentLiveData().postValue("搜索成功，${result.data}")
            else getContentLiveData().postValue("比对失败，${result.data}")*/
        }
    }

}