package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.llj.living.R
import com.llj.living.custom.ext.save
import com.llj.living.data.const.Const
import com.llj.living.utils.ToastUtils

class LoginViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    fun getPassWordLiveData() = getLiveDataForKey<String>(Const.UserPwdLogin)
    fun getUserNameLiveData() = getLiveDataForKey<String>(Const.UserNameLogin)
    fun getRememberPwdLiveData() = getLiveDataForKey<Boolean>(Const.UserRememberPwdLogin)

    suspend fun login(): Boolean {
        if (!checkInfo()) return false
        //登录验证
        //服务器验证成功后
        return savedSp()
    }

    /**
     * 保存用户名 密码
     */
    private fun savedSp(): Boolean {
        val spIsSuc = getSP(Const.SPUser).save {
            putString(Const.SPUserPwdLogin, getPassWordLiveData().value)
            putString(Const.SPUserNameLogin, getUserNameLiveData().value)
            putBoolean(Const.SPUserRememberPwdLogin, getRememberPwdLiveData().value!!)
        }
        return if (!spIsSuc) {
            ToastUtils.toastShort("登录${getApp().getString(R.string.sp_saved_fail)}")
            false
        } else true
    }

    private fun checkInfo(): Boolean {
        getApp().apply {
            if (getUserNameLiveData().value.isNullOrEmpty()) {
                ToastUtils.toastShort(resources.getString(R.string.user_name_is_null))
                return false
            }
            if (getPassWordLiveData().value.isNullOrEmpty()) {
                ToastUtils.toastShort(resources.getString(R.string.user_pwd_is_null))
                return false
            }
            //login----
        }
        return true
    }

    fun loadUserData() {
        getSP(Const.SPUser).let { sp ->
            if (sp.contains(Const.SPUserRememberPwdLogin)) {
                val spIsSaved = sp.getBoolean(Const.SPUserRememberPwdLogin, false)//是否记住密码
                if (spIsSaved) { //如果保存 则赋值给界面
                    if (sp.contains(Const.SPUserNameLogin)) {
                        getUserNameLiveData().postValue(sp.getString(Const.SPUserNameLogin, ""))
                    }
                    if (sp.contains(Const.SPUserPwdLogin)) {
                        getPassWordLiveData().postValue(sp.getString(Const.SPUserPwdLogin, ""))
                    }
                }
                getRememberPwdLiveData().postValue(spIsSaved)
            }

        }
    }

    fun getApp() = getApplication<Application>()
}