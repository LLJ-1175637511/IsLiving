package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.SavedStateHandle

class LoginViewModel(application: Application,savedStateHandle: SavedStateHandle):BaseViewModel(application, savedStateHandle) {

    fun getPassWordLiveData() = getLiveDataForKey<String>("pwd")

}