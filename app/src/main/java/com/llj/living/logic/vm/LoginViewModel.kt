package com.llj.living.logic.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.llj.living.R
import com.llj.living.custom.ext.save
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.data.const.cons
import com.llj.living.utils.ToastUtils

class LoginViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    fun getPassWordLiveData() = getLiveDataForKey<String>(cons.UserPwdLogin)
    fun getUserNameLiveData() = getLiveDataForKey<String>(cons.UserNameLogin)
    fun getRememberPwdLiveData() = getLiveDataForKey<Boolean>(cons.UserRememberPwdLogin)

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
        getApplication<Application>().apply {
            val spIsSuc = getSharedPreferences(cons.USER, Context.MODE_PRIVATE).save {
                putString(cons.SPUserPwdLogin, getPassWordLiveData().value)
                putString(cons.SPUserNameLogin, getUserNameLiveData().value)
                putBoolean(cons.SPUserRememberPwdLogin, getRememberPwdLiveData().value!!)
            }
            return if (!spIsSuc) {
                ToastUtils.toastShort("登录${resources.getString(R.string.sp_saved_fail)}")
                false
            } else true
        }
    }

    private fun checkInfo(): Boolean {
        getApplication<Application>().apply {
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

}