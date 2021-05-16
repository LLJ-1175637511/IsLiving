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
        if (entLoc.first == 0.0 || entLoc.second == 0.0) {
            setToast("获取服务器定位参数错误")
            return null
        }
        //获取两坐标点距离（米）
        val distance = LonLatUtils.getDistance(entLoc, loc)
        LogUtils.d(TAG, "distance:${distance}")
        val degree = MyApplication.getDistance()
        if (distance > degree) { //如果 当前定位 到 规定定位 的距离 大于2公里
            setToast("超出服务范围")
            return null
        }
        return true
    }

    /**
     * isLogined:判断是否已经登录 未登录时 请求网络 不检测定位信息
     */
    suspend inline fun <reified T> quickRequest(
        isLogined: Boolean = true,
        crossinline block: suspend () -> BaseBean
    ): T? = viewModelScope.async {
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