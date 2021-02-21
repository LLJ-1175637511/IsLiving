package com.llj.living

import androidx.lifecycle.Observer
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivityLoginBinding
import com.llj.living.logic.vm.LoginViewModel
import com.llj.living.ui.activity.BaseActivity
import com.llj.living.utils.ToastUtils

class LoginActivity:BaseActivity<ActivityLoginBinding>(){

    override fun getLayoutId(): Int = R.layout.activity_login

    private lateinit var viewModel:LoginViewModel

    init {
        setToolbar(ToolbarConfig(title = "hello",isShowBack = true,isShowMenu = false))
    }

    override fun init() {
        initView()
        initListener()
    }

    private fun initView() {
        viewModel = initViewModel<LoginViewModel>()
        viewModel.getPassWordLiveData().observe(this, Observer {
            ToastUtils.toastShort("haha")
        })
        getDataBinding().loginVm = viewModel
    }

    private fun initListener() {
        getDataBinding().btLoginActivity.setOnClickListener {
            viewModel.getPassWordLiveData().postValue((0..5).random().toString())
        }
    }

}