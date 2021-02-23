package com.llj.living.ui.activity

import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.custom.ext.baseObserver
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivityLoginBinding
import com.llj.living.logic.vm.LoginViewModel
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_login

    private lateinit var viewModel: LoginViewModel

    override fun init() {
        setToolbar(ToolbarConfig(title = "LoginActivity", isShowBack = false, isShowMenu = false))
        initListener()
        initVM()
    }

    private fun initVM() {
        viewModel = initViewModel<LoginViewModel>()

        viewModel.getRememberPwdLiveData().baseObserver(this) {
            getDataBinding().cbRememPwdLogin.isChecked = it
        }

        getDataBinding().loginVm = viewModel

        viewModel.loadUserData()
    }

    private fun initListener() {
        getDataBinding().btLoginActivity.setOnClickListener {
            viewModel.getPassWordLiveData().postValue((0..5).random().toString())
        }
        getDataBinding().cbRememPwdLogin.apply {
            setOnClickListener {
                viewModel.getRememberPwdLiveData().postValue(this.isChecked)
            }
        }
        getDataBinding().btLoginActivity.setOnClickListener {
            lifecycleScope.launch {
                val isSuc = viewModel.login()
                if (isSuc) {
                    startCommonActivity<MainActivity>()
                    finish()
                }
            }
        }
    }

}