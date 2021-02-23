package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.llj.living.custom.ext.save
import com.llj.living.data.const.Const
import com.llj.living.net.repository.FaceAuthRepository
import com.llj.living.utils.LogUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.launch

class FaceAuthViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    private val TAG = this.javaClass.simpleName

    fun getTokenLiveData() = getLiveDataForKey<String>(Const.TokenFaceAuth)

    fun getPhotoLiveData() = getLiveDataForKey<String>(Const.FaceAuthPhoto)

    fun getToken() {
        val sp = getSP(Const.SPBaiduToken)
        LogUtils.d(TAG,"${sp.contains(Const.SPBaiduTokenString)}")
        if (sp.contains(Const.SPBaiduTokenString)) {
            val periodTime = sp.getLong(Const.SPBaiduTokenPeriod, 0L)
            val currentTime = System.currentTimeMillis() / 1000
            LogUtils.d(TAG,"periodTime:${periodTime} currentTime:${currentTime}")
            if (periodTime - 60 * 60 * 24 * 2 > currentTime) {
                return //如果未过期 则不需要请求token
            }
        }
        viewModelScope.launch {
            val tokenBean = FaceAuthRepository.sendTokenRequest()
            if (tokenBean.isSuc) { //请求成功则保存到sp中
                val savedSp = getSP(Const.SPBaiduToken).save {
                    putString(Const.SPBaiduTokenString, tokenBean.data)
                    val periodTime = System.currentTimeMillis() / 1000 + tokenBean.expiresIn
                    putLong(Const.SPBaiduTokenPeriod, periodTime)
                }
                if (savedSp) ToastUtils.toastShort("token saved suc")
                else ToastUtils.toastShort("token saved err")
                LogUtils.d(TAG, "suc:${tokenBean}")
            } else {
                LogUtils.d(TAG, "err:${tokenBean}")
            }
        }
    }
}