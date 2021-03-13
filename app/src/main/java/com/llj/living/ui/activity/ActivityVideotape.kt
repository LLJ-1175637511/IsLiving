package com.llj.living.ui.activity

import com.llj.living.R
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivityVideotapeBinding

class ActivityVideotape:BaseActivity<ActivityVideotapeBinding>() {

    override fun getLayoutId() = R.layout.activity_videotape

    override fun init() {
        setToolbar(ToolbarConfig("录像",isShowBack = true,isShowMenu = false))
    }

}