package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.llj.living.R
import com.llj.living.application.MyApplication
import com.llj.living.custom.ext.commonLaunch
import com.llj.living.custom.ext.getSP
import com.llj.living.custom.ext.save
import com.llj.living.custom.ext.versionToInt
import com.llj.living.data.bean.VersionBean
import com.llj.living.data.const.Const
import com.llj.living.data.enums.VersionUpdateEnum
import com.llj.living.net.repository.SystemRepository
import com.llj.living.utils.LogUtils

class LoginViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    val passWordLiveData = MutableLiveData<String>("")
    val userNameLiveData = MutableLiveData<String>("")
    val rememberPwdLiveData = MutableLiveData<Boolean>(false)

    //是否允许登录
    private val _loginResultLiveData = MutableLiveData<Boolean>(false)
    val loginResultLiveData: LiveData<Boolean> = _loginResultLiveData

    //是否验证版本
    val isRemindUpdateLiveData = MutableLiveData<Boolean>(false)

    private val _isDialogVersionLiveData = MutableLiveData<String>("${getApplication<MyApplication>().getString(
        R.string.new_version)}${MyApplication.CURRENT_VERSION}")
    val isDialogVersionLiveData:LiveData<String> = _isDialogVersionLiveData

    //获取版本数据 Pair(first,second) first(0):需要更新  first(1):强制更新
    private val _versionLiveData = MutableLiveData<Pair<VersionUpdateEnum,VersionBean>>()
    val versionLiveData: LiveData<Pair<VersionUpdateEnum,VersionBean>> = _versionLiveData

    private var checkVersionIsOk = false

    fun checkVersion() = commonLaunch{
        if (!checkVersionIsOk) {
            //获取版本
            val versionBean = quickRequest<VersionBean> {
                SystemRepository.getVersionRequest(MyApplication.CURRENT_VERSION)
            }?:return@commonLaunch
            if (versionBean.enforce == 1){
                _versionLiveData.postValue(Pair(VersionUpdateEnum.FORCE,versionBean))
                return@commonLaunch
            }
            val newVersion = versionBean.newversion.versionToInt()
            val oldVersion = MyApplication.CURRENT_VERSION.versionToInt()
            LogUtils.d("${this.javaClass.simpleName}BASE","$oldVersion $newVersion")
            if (newVersion == null || oldVersion == null) {
                setToast("版本号错误")
                return@commonLaunch
            } else {
                if (oldVersion < newVersion) {
                    //提示可更新
                    if (!getSP(Const.SPMySqlNet).getBoolean(Const.SPMySqlTodayReminderUpdate, false)) {
                        _versionLiveData.postValue(Pair(VersionUpdateEnum.REMIND,versionBean))
                        return@commonLaunch
                    }
                }
                checkVersionIsOk = true
                login()
            }
        } else { // 版本可用
            login()
        }
    }

    fun login(){
        //登录验证
        _loginResultLiveData.postValue(true)
        savedSp()
    }

    /**
     * 保存用户名 密码
     */
    private fun savedSp() = getSP(Const.SPUser).save {
        putString(Const.SPUserPwdLogin, passWordLiveData.value)
        putString(Const.SPUserNameLogin, userNameLiveData.value)
        putBoolean(Const.SPUserRememberPwdLogin, rememberPwdLiveData.value!!)
    }

    fun loadUserData() {
        getSP(Const.SPUser).let { sp ->
            if (sp.contains(Const.SPUserRememberPwdLogin)) {
                val spIsSaved = sp.getBoolean(Const.SPUserRememberPwdLogin, false)//是否记住密码
                if (spIsSaved) { //如果保存 则赋值给界面
                    if (sp.contains(Const.SPUserNameLogin)) {
                        userNameLiveData.postValue(sp.getString(Const.SPUserNameLogin, ""))
                    }
                    if (sp.contains(Const.SPUserPwdLogin)) {
                        passWordLiveData.postValue(sp.getString(Const.SPUserPwdLogin, ""))
                    }
                }
                rememberPwdLiveData.postValue(spIsSaved)
            }
        }
    }

    fun loadNewApk() {
        //下载吸纳不能apk
    }

}