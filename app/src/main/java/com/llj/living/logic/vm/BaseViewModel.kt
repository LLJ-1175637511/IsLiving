package com.llj.living.logic.vm

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.llj.living.application.MyApplication
import com.llj.living.custom.ext.*
import com.llj.living.data.bean.BaseBean
import com.llj.living.data.const.Const
import com.llj.living.data.enums.BaseDataEnum
import com.llj.living.net.repository.FaceAuthRepository
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val TAG = "${this.javaClass.simpleName}BASE"

    private val _locationLiveData = MutableLiveData<Pair<Double, Double>>(Pair(0.0, 0.0))
    val locationLiveData: LiveData<Pair<Double, Double>> = _locationLiveData

    private val _toastMsgLiveData = MutableLiveData<String>()
    val toastMsgLiveData: LiveData<String> = _toastMsgLiveData

    /*  private val _tokenLiveData = MutableLiveData<String>()
      val tokenLiveData: LiveData<String> = _tokenLiveData*/

    fun <T : Any> getLiveDataForKey(name: String, type: Class<T>): MutableLiveData<T> {
        try {
            if (!savedStateHandle.contains(name)) { //如果savedStateHandle没有包含该key的实例 则创建一个默认值
                when (type.simpleName) { //初始化key对应的基础数据类型
                    BaseDataEnum.Boolean.name -> savedStateHandle.set(name, false)
                    BaseDataEnum.Character.name -> savedStateHandle.set(name, '\u0000')
                    BaseDataEnum.Float.name -> savedStateHandle.set(name, 0.0f)
                    BaseDataEnum.Double.name -> savedStateHandle.set(name, 0.0)
                    BaseDataEnum.Integer.name -> savedStateHandle.set(name, 0)
                    BaseDataEnum.Short.name -> savedStateHandle.set(name, 0)
                    BaseDataEnum.Long.name -> savedStateHandle.set(name, 0L)
                    BaseDataEnum.String.name -> savedStateHandle.set(name, "")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtils.d(TAG, e.message.toString())
        }
        return savedStateHandle.getLiveData(name)
    }

    fun setLocation(location: Pair<Double, Double>) {
        _locationLiveData.postValue(location)
    }

    fun setToast(msg: String) {
        _toastMsgLiveData.postValue(msg)
    }

    fun setErrToast(msg: String) {
        if (true){ //开发调试用
            _toastMsgLiveData.postValue(msg)
        }
    }

    suspend fun getToken() = withContext<String>(Dispatchers.IO) {
        val sp = getSP(Const.SPBaiduToken)
        if (sp.contains(Const.SPBaiduTokenPeriod)) {
            val periodTime = sp.getLong(Const.SPBaiduTokenPeriod, 0L)
            val currentTime = System.currentTimeMillis() / 1000
            LogUtils.d(TAG, "periodTime:${periodTime} currentTime:${currentTime}")
            if (periodTime - 60 * 60 * 24 * 2 > currentTime) {
                return@withContext getSP(Const.SPBaiduToken).getString(
                    Const.SPBaiduTokenString,
                    ""
                ).toString()//如果未过期 则不需要请求token
            }
        }
        val tokenBean = FaceAuthRepository.sendTokenRequest()
        LogUtils.d(TAG, "suc:${tokenBean}")
        if (tokenBean.isSuc) { //请求成功则保存到sp中
            val savedSp = getSP(Const.SPBaiduToken).save {
                putString(Const.SPBaiduTokenString, tokenBean.data)
                val periodTime = System.currentTimeMillis() / 1000 + tokenBean.expiresIn
                putLong(Const.SPBaiduTokenPeriod, periodTime)
            }
            return@withContext if (savedSp) {
                tokenBean.data
            } else {
                false.toString()
            }
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
    }

    fun checkLocation(block: () -> Unit) {
        //登录验证
        if (locationLiveData.value!!.first == 0.0 && locationLiveData.value!!.second == 0.0) {
            setToast("验证定位中 请稍后再试")
            return
        }
        block()
    }

    suspend inline fun <reified T> realRequest(crossinline block: suspend () -> BaseBean): Pair<T?, Exception?> {
        var exc: Exception? = null
        var data: T? = null
        try {
            val baseBean = block()
            LogUtils.d("${this.javaClass.simpleName}BASE", baseBean.toString())
            if (baseBean.code.isCodeSuc() && baseBean.msg.isMsgSuc()) {
                data = baseBeanConverter<T>(baseBean)
                val sp = getSP(Const.SPMySqlNet)
                val token = baseBean.token
                if (token.isNotEmpty()) {
                    sp.save {
                        putString(Const.SPMySqlToken, token)
                    }
                }
            } else {
                exc = Exception("错误--> errCode:${baseBean.code} errMsg:${baseBean.msg}")
            }
        } catch (e: Exception) {
            LogUtils.d("${this.javaClass.simpleName}BASE", e.message.toString())
            exc = e
        }
        return Pair(data, exc)
    }

    suspend inline fun <reified T> quickRequest(crossinline block: suspend () -> BaseBean): T? =
        commonAsych commonLaunch@{
            val beanPair = realRequest<T> {
                block()
            }
            val exception = beanPair.second
            if (exception != null) {
                setErrToast(exception.message.toString())
                return@commonLaunch null
            }
            beanPair.first
        }.await()


    fun <T> getLiveDataListForKey(
        name: String,
        type: MutableList<T>
    ): MutableLiveData<MutableList<T>> {
        if (!savedStateHandle.contains(name)) { //如果savedStateHandle没有包含该key的实例 则创建一个默认值
            try {
                savedStateHandle.set(name, type)
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.d(TAG, e.message.toString())
            }
        }
        return savedStateHandle.getLiveData(name)
    }

    inline fun <reified T : Any> getLiveDataForKey(name: String): MutableLiveData<T> =
        getLiveDataForKey(name, T::class.java)

    inline fun <reified T> getLiveDataListForKey(name: String): MutableLiveData<MutableList<T>> =
        getLiveDataListForKey(name, mutableListOf<T>())

    fun getSavedHandle() = savedStateHandle

    /*fun getSP(key: String): SharedPreferences =
        getApplication<MyApplication>().getSharedPreferences(key, Context.MODE_PRIVATE)*/

}