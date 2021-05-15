package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.*
import com.llj.living.application.MyApplication
import com.llj.living.custom.ext.realRequest
import com.llj.living.data.bean.BaseBean
import com.llj.living.utils.LogUtils
import com.llj.living.utils.LonLatUtils
import kotlinx.coroutines.async

abstract class BaseViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    val TAG = "${this.javaClass.simpleName}BASE"

    private val _toastMsgLiveData = MutableLiveData<String>()
    val toastMsgLiveData: LiveData<String> = _toastMsgLiveData

    fun setToast(msg: String) {
        if (true) { //开发调试用
            _toastMsgLiveData.postValue(msg)
        }
    }

    fun setErrToast(msg: String) {
        if (true) { //开发调试用
            _toastMsgLiveData.postValue(msg)
        }
    }

    fun checkLocation(): Boolean? {
        val entLoc = MyApplication.getEntLocation()
        val loc = MyApplication.getLocation()
        if (loc.first == 0.0 || loc.second == 0.0) {
            setToast("获取本机定位信息失败 请返回重试")
            return null
        }
        /* if (entLoc.first == 0.0 || entLoc.second == 0.0) {
             setToast("获取服务器定位参数错误")
             return null
         }*/
        //获取两坐标点距离（米）
        val distance = LonLatUtils.getDistance(entLoc, loc)
        LogUtils.d(TAG, "distance:${distance}")
        /* if (distance > 2000) { //如果 当前定位 到 规定定位 的距离 大于2公里
             setToast("超出服务范围")
             return null
         }*/
        return true
    }

    /*suspend fun getToken() = withContext<String>(Dispatchers.IO) {
        val sp = getSP(Const.SPBaidu)
        if (sp.contains(Const.SPBaiduTokenPeriod)) {
            val periodTime = sp.getLong(Const.SPBaiduTokenPeriod, 0L)
            val currentTime = System.currentTimeMillis() / 1000
            LogUtils.d(TAG, "periodTime:${periodTime} currentTime:${currentTime}")
            if (periodTime - 60 * 60 * 24 * 2 > currentTime) { //最后两天过期的情况下 请求新的token
                return@withContext sp.getString(Const.SPBaiduTokenString, "").toString()//如果未过期 则不需要请求token
            }
        }
        val tokenBean = FaceAuthRepository.sendTokenRequest()
        LogUtils.d(TAG, "suc:${tokenBean}")
        if (tokenBean.isSuc) { //请求成功则保存到sp中
            val savedSp = sp.save {
                putString(Const.SPBaiduTokenString, tokenBean.data)
                val periodTime = System.currentTimeMillis() / 1000 + tokenBean.expiresIn
                putLong(Const.SPBaiduTokenPeriod, periodTime)
            }
            return@withContext if (savedSp) { tokenBean.data } else { false.toString() }
        } else {
            LogUtils.d(TAG, "err:${tokenBean}")
            return@withContext false.toString()
        }
    }

    suspend fun checkBaiduTokenRequest(block: suspend (token: String) -> Unit) {
        val token = getToken()
        if (token == false.toString()) {
            withContext(Dispatchers.Main) {
                setToast("token get failed")
            }
            return
        } else {
            block(token)
        }
    }*/

    /**
     * isLogined:判断是否已经登录 未登录时 请求网络 不检测定位信息
     */
    suspend inline fun <reified T> quickRequest(
        isLogined: Boolean = true,
        crossinline block: suspend () -> BaseBean
    ): T? = viewModelScope.async{
        if (isLogined) {
            checkLocation() ?: return@async null
        }
        val beanPair = realRequest<T> {
            block()
        }
        val exception = beanPair.second
        if (exception != null) {
            setErrToast(exception.message.toString())
            return@async null
        }
        beanPair.first
    }.await()


    fun getSavedHandle() = savedStateHandle

}