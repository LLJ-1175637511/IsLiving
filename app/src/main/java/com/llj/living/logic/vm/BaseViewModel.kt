package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle

abstract class BaseViewModel(application: Application,private val savedStateHandle: SavedStateHandle):AndroidViewModel(application) {

    fun <T> getLiveDataForKey(name:String, type:Class<T>):MutableLiveData<T>{
        if (!savedStateHandle.contains(name)){
            val temp:T = type.newInstance()
            savedStateHandle.set(name,temp)
        }
        return savedStateHandle.getLiveData(name)
    }

    inline fun <reified T> getLiveDataForKey(name: String):MutableLiveData<T> = getLiveDataForKey(name,T::class.java)

    fun getSavedHandle() = savedStateHandle
}