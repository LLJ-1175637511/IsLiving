package com.llj.living.ui.activity

import com.llj.living.R
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivitySupplementInfoBinding

class ActivitySuppleInfo:BaseActivity<ActivitySupplementInfoBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_supplement_info

    override fun init() {
        setToolbar(ToolbarConfig("补录信息",isShowBack = true,isShowMenu = false))
    }

}