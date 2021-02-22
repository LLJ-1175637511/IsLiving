package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.llj.living.data.enums.BaseDataEnum
import com.llj.living.utils.LogUtils

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
        }
        return savedStateHandle.getLiveData(name)
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
            }
        }
        return savedStateHandle.getLiveData(name)
    }

    inline fun <reified T : Any> getLiveDataForKey(name: String): MutableLiveData<T> =
        getLiveDataForKey(name, T::class.java)

    inline fun <reified T> getLiveDataListForKey(name: String): MutableLiveData<MutableList<T>> =
        getLiveDataListForKey(name, mutableListOf<T>())

    fun getSavedHandle() = savedStateHandle
}