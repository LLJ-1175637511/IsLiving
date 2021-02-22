package com.llj.living

import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivityLoginBinding
import com.llj.living.logic.vm.LoginViewModel
import com.llj.living.ui.activity.BaseActivity
import com.llj.living.ui.activity.MainActivity
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.launch

class LoginActivity:BaseActivity<ActivityLoginBinding>(){

    override fun getLayoutId(): Int = R.layout.activity_login

    private lateinit var viewModel:LoginViewModel

    init {
        setToolbar(ToolbarConfig(title = "LoginActivity",isShowBack = true,isShowMenu = false))
    }

    override fun init() {
        initListener()
        initVM()
    }

    private fun initVM() {
        viewModel = initViewModel<LoginViewModel>()

        /*viewModel.getRememberPwdLiveData().baseObserver(this) {
            ToastUtils.toastShort("记住密码:${it}")
        }*/

        viewModel.getRememberPwdLiveData().observe(this, Observer {
            ToastUtils.toastShort("记住密码:${it}")
        })

        getDataBinding().loginVm = viewModel

        viewModel.test()
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
        mutableListOf<String>()
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