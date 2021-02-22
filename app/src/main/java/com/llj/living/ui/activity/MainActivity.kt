package com.llj.living.ui.activity

import com.llj.living.R
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivityMainBinding

class MainActivity:BaseActivity<ActivityMainBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_main

    init {
        setToolbar(ToolbarConfig(title = "MainActivity",isShowBack = true,isShowMenu = true))
    }

}