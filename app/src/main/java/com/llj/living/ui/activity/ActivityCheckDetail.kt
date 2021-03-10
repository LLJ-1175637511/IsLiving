package com.llj.living.ui.activity

import com.llj.living.R
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivityCheckDetailBinding

class ActivityCheckDetail:BaseActivity<ActivityCheckDetailBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_check_detail

    override fun init() {
        setToolbar(ToolbarConfig(getString(R.string.check_detail),isShowBack = true,isShowMenu = true))
    }

}