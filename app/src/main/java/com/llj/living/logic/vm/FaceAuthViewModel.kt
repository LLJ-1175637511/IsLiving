package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.llj.living.custom.ext.save
import com.llj.living.data.const.Const
import com.llj.living.net.config.NetConfig
import com.llj.living.net.repository.FaceAuthRepository
import com.llj.living.utils.LogUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FaceAuthViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    private val TAG = this.javaClass.simpleName

    fun getTokenLiveData() = getLiveDataForKey<String>(Const.TokenFaceAuth)

    fun getPhotoLiveData() = getLiveDataForKey<String>(Const.FaceAuthPhoto)

    fun registerFace(map: Map<String,String>){
        viewModelScope.launch {
            val token = withContext(Dispatchers.IO){
                getToken()
            }
            if (token==false.toString()) {
                withContext(Dispatchers.Main){
                    ToastUtils.toastShort("token get failed")
                }
                return@launch
            }else{
                val result = FaceAuthRepository.sendRegisterFaceRequest(token,map)
                LogUtils.d(TAG,result)
            }
        }
    }


}