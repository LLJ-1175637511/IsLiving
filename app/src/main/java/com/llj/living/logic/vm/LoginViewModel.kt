package com.llj.living.logic.vm

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Environment
import android.telephony.TelephonyManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.llj.living.application.MyApplication
import com.llj.living.custom.ext.*
import com.llj.living.data.bean.LoginBean
import com.llj.living.data.bean.VersionBean
import com.llj.living.data.const.Const
import com.llj.living.data.enums.VersionUpdateEnum
import com.llj.living.net.config.SysNetConfig.buildLoginMap
import com.llj.living.net.repository.SystemRepository
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class LoginViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    val passWordLiveData = MutableLiveData<String>("")
    val userNameLiveData = MutableLiveData<String>("")
    val rememberPwdLiveData = MutableLiveData<Boolean>(false)

    //获取版本数据 Pair(first,second) first(0):需要更新  first(1):强制更新
    private val _versionLiveData = MutableLiveData<Pair<VersionUpdateEnum, VersionBean>>()
    val versionLiveData: LiveData<Pair<VersionUpdateEnum, VersionBean>> = _versionLiveData

    private val _loginLiveData = MutableLiveData<LoginBean>()
    val loginLiveData: LiveData<LoginBean> = _loginLiveData

    private var checkVersionIsOk = false
    private var currentImei: String? = null

    private val _loadApkProgressLD = MutableLiveData<Int>(0)
    val loadApkProgressLD: LiveData<Int> = _loadApkProgressLD

    private val _cancelLoadApkDialogLD = MutableLiveData<Boolean>()
    val cancelLoadApkDialogLD = _cancelLoadApkDialogLD

    private val _installLiveData = MutableLiveData<File>()
    val installLiveData: LiveData<File> = _installLiveData

    private lateinit var downLoadJob: Job

    fun checkVersion() = commonLaunch {
        currentImei = getImei()
        currentImei ?: return@commonLaunch
        if (!checkVersionIsOk) {
            //获取版本
            val versionBean = quickRequest<VersionBean>(isLogined = false) {
                SystemRepository.getVersionRequest(MyApplication.CURRENT_VERSION)
            } ?: return@commonLaunch
            if (versionBean.enforce == 1) {
                _versionLiveData.postValue(Pair(VersionUpdateEnum.FORCE, versionBean))
                return@commonLaunch
            }
            val newVersion = versionBean.newversion.versionToInt()
            val oldVersion = MyApplication.CURRENT_VERSION.versionToInt()
            LogUtils.d(TAG, "oldVersion:$oldVersion newVersion:$newVersion")
            if (newVersion == null || oldVersion == null) {
                setToast("版本号错误")
                return@commonLaunch
            } else {
                if (oldVersion < newVersion) {
                    //提示可更新
                    if (!getSP(Const.SPMySqlNet).getBoolean(
                            Const.SPMySqlTodayReminderUpdate,
                            false
                        )
                    ) {
                        _versionLiveData.postValue(Pair(VersionUpdateEnum.REMIND, versionBean))
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

    fun login() = tryExceptionLaunch(errTips = "登录信息") {
        //登录验证
        LogUtils.d(
            TAG,
            "imei:${currentImei} name:${userNameLiveData.value} pass:${passWordLiveData.value}"
        )
        val loginBean = quickRequest<LoginBean>(isLogined = false) {
            val lat = MyApplication.getLocation().second.toString()
            val lng = MyApplication.getLocation().first.toString()
            val map =
                buildLoginMap(
                    userNameLiveData.value!!,
                    passWordLiveData.value!!,
                    currentImei!!,
                    lat,
                    lng
                )
            SystemRepository.loginRequest(map)
        } ?: return@tryExceptionLaunch
        MyApplication.setDistance(loginBean.ent_degree)
        MyApplication.setEntLocation(Pair(loginBean.ent_lng, loginBean.ent_lat))
        //地理位置验证 登录后默认 isLogined = true 请求网络时自动验证
        checkLocation() ?: return@tryExceptionLaunch
        getSP(Const.SPBaidu).save {
            putString(Const.SPBaiduTokenString, loginBean.ent_data)
        }
        _loginLiveData.postValue(loginBean)
        savedUserSp(loginBean.token, loginBean.ent_id)
    }

    private fun getImei() = try {
        val mTelephonyMgr =
            getApplication<MyApplication>().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            mTelephonyMgr.imei
        } else mTelephonyMgr.deviceId
    } catch (e: Exception) {
        e.printStackTrace()
        "000000000000000"
    }

    /**
     * 保存用户名 密码
     */
    private fun savedUserSp(token: String, entId: Int) = getSP(Const.SPUser).save {
        putString(Const.SPUserPwdLogin, passWordLiveData.value)
        putString(Const.SPUserNameLogin, userNameLiveData.value)
        putString(Const.SPUserTokenLogin, token)
        putInt(Const.SPUserEntId, entId)
        putBoolean(Const.SPUserRememberPwdLogin, rememberPwdLiveData.value!!)
    }

    fun loadUserData() {
        getSP(Const.SPUser).let { sp ->
            if (sp.contains(Const.SPUserRememberPwdLogin)) {
                val spIsSaved = sp.getBoolean(Const.SPUserRememberPwdLogin, false)//是否记住密码
                if (spIsSaved) { //如果保存 则赋值给界面
                    userNameLiveData.postValue(sp.getString(Const.SPUserNameLogin, ""))
                    passWordLiveData.postValue(sp.getString(Const.SPUserPwdLogin, ""))
                }
                rememberPwdLiveData.postValue(spIsSaved)
            }
        }
    }

    fun loadNewApk() {
        if (this::downLoadJob.isInitialized) return
        try {
            val extrasFile =
                getApplication<MyApplication>().applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            downLoadJob = viewModelScope.launch(Dispatchers.IO) {
                //下载最新apk
                val apkStrArray = versionLiveData.value!!.second.downloadurl.split('/')
                val apkName = apkStrArray[apkStrArray.size - 1]
                LogUtils.d(TAG, "apkName:${apkName}")
                val response = SystemRepository.loadAPKRequest(apkName)
                val ios = response.byteStream()
                val apkLength = response.contentLength()
                LogUtils.d(TAG, "apkLength:${apkLength}")
                var readLength = 0
                val tempFile = File(extrasFile, apkName)
                if (tempFile.exists()) {
                    tempFile.delete()
                }
                tempFile.createNewFile()
                val fos = FileOutputStream(tempFile)
                val bytes = ByteArray(1024)
                var currentLength = 0L
                while (ios.read(bytes).also { readLength = it } != -1) {
                    currentLength += readLength
                    fos.write(bytes, 0, readLength)
                    _loadApkProgressLD.postValue((currentLength * 100 / apkLength).toInt())
                }
                fos.flush()
                fos.close()
                ios.close()
                _cancelLoadApkDialogLD.postValue(true)
                _installLiveData.postValue(tempFile)
            }
        } catch (e: Exception) {
            _cancelLoadApkDialogLD.postValue(true)
            setErrToast("file err:${e.message}")
            LogUtils.d(TAG, "file err:${e.message}")
            e.printStackTrace()
        }
    }

    fun cancelLoadApk() {
        if (this::downLoadJob.isInitialized) { //如果下载任务已经开始 则自动取消
            downLoadJob.cancel()
        }
    }

}