package com.llj.living.ui.activity

import androidx.activity.viewModels
import com.llj.living.R
import com.llj.living.application.MyApplication
import com.llj.living.custom.ext.baseObserver
import com.llj.living.custom.ext.startCommonFinishedActivity
import com.llj.living.custom.ext.toastShort
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivityLoginBinding
import com.llj.living.logic.vm.LoginViewModel

class LoginActivity : BaseBLActivity<ActivityLoginBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_login

    private val viewModel by viewModels<LoginViewModel>()

    override fun init() {
        setToolbar(ToolbarConfig(title = "用户登录", isShowBack = false, isShowMenu = false))
        initVM()
        initLD()
    }

    private fun initLD() {
        viewModel.loginResultLiveData.baseObserver(this) { isSuc ->
            if (isSuc) {
                startCommonFinishedActivity<MainActivity>()
            }
        }
        viewModel.toastMsgLiveData.baseObserver(this) {
            if (it.isNotEmpty()) toastShort(it)
        }
        locationLiveData.baseObserver(this) {
            viewModel.setLocation(it)
        }
    }

    private fun initVM() {
        getDataBinding().loginVm = viewModel
        val versionStr =
            "${resources.getString(R.string.user_version)}${MyApplication.CURRENT_VERSION}"
        getDataBinding().tvVersionLogin.text = versionStr
        viewModel.loadUserData()
    }

}