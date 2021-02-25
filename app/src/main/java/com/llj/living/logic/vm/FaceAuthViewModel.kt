package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.llj.living.data.const.Const
import com.llj.living.data.enums.ModifyFaceType
import com.llj.living.net.repository.FaceAuthRepository
import com.llj.living.utils.LogUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FaceAuthViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    private val TAG = this.javaClass.simpleName

    fun getContentLiveData() = getLiveDataForKey<String>(Const.FaceAuthContent)

    fun getPhotoLiveData() = getLiveDataForKey<String>(Const.FaceAuthPhoto)

    fun getPhotoIdLiveData() = getLiveDataForKey<String>(Const.FaceAuthPhotoId)

    fun modifyFace(
        map: Map<String, String>,
        type: ModifyFaceType
    ) = viewModelScope.launch {
        checkTokenAndSendRequest { token ->
            val result = FaceAuthRepository.sendModifyFaceRequest(token, map, type)
            val msg = if (result.isSuc) {
                getPhotoIdLiveData().postValue(result.data)
                "操作成功 唯一码：${result.data}"
            } else "操作失败 code:${result.data}"
            getContentLiveData().postValue(msg)
            LogUtils.d(TAG, msg)
        }
    }

    fun deleteFace(map: Map<String, String>) = viewModelScope.launch {
        checkTokenAndSendRequest { token ->
            val result = FaceAuthRepository.sendDeleteFaceRequest(token, map)
            val msg =
                if (result.error_code == 0 && result.error_msg == "SUCCESS") "删除成功 唯一码：${getPhotoIdLiveData().value}"
                else "删除失败 msg:${result.error_msg}"
            getContentLiveData().postValue(msg)
            LogUtils.d(TAG, msg)
        }
    }
}