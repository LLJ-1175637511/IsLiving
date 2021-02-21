package com.llj.living

import com.llj.living.databinding.ActivitySplashBinding
import com.llj.living.ui.activity.BaseActivity

class TestActivity:BaseActivity<ActivitySplashBinding>(){

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun init() {
        setIsFullScreen(true)

    }

    fun initView() {

    }

    fun initListener() {

    }

}