package com.llj.living.application

import android.app.Application
import com.llj.living.utils.LogUtils
import com.llj.living.utils.ToastUtils

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        ToastUtils.init(this)
    }

}