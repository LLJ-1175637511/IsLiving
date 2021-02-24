package com.llj.living.logic.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.llj.living.custom.ext.save
import com.llj.living.data.const.Const
import com.llj.living.data.enums.BaseDataEnum
import com.llj.living.net.repository.FaceAuthRepository
import com.llj.living.utils.LogUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val TAG = this.javaClass.simpleName

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
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtils.d(TAG, e.message.toString())
        }
        return savedStateHandle.getLiveData(name)
    }

    suspend fun getToken() =
        withContext<String>(Dispatchers.IO) {
            val sp = getSP(Const.SPBaiduToken)
            if (sp.contains(Const.SPBaiduTokenPeriod)) {
                val periodTime = sp.getLong(Const.SPBaiduTokenPeriod, 0L)
                val currentTime = System.currentTimeMillis() / 1000
                LogUtils.d(TAG, "periodTime:${periodTime} currentTime:${currentTime}")
                if (periodTime - 60 * 60 * 24 * 2 > currentTime) {
                    return@withContext getSP(Const.SPBaiduToken).getString(Const.SPBaiduTokenString,"").toString()//如果未过期 则不需要请求token
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
                    ToastUtils.toastShort("token saved suc")
                    return@withContext getSP(Const.SPBaiduToken).getString(Const.SPBaiduTokenString,"").toString()
                } else {
                    ToastUtils.toastShort("token saved err")
                    false.toString()
                }
            } else {
                LogUtils.d(TAG, "err:${tokenBean}")
                return@withContext false.toString()
            }
        }



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

    fun getSP(key: String) =
        getApplication<Application>().getSharedPreferences(key, Context.MODE_PRIVATE)
}