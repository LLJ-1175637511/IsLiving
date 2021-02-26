package com.llj.living.application

import android.app.Application
import com.llj.living.utils.ToastUtils

class MyApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        ToastUtils.init(this.applicationContext)
    }

    companion object{
        const val ApiKey = "f05CbSUbz4fpk5EFMfwNzgdh"
        const val SecretKey = "jFRKm4I4RAZ7YS7svK3xOQkuhfTcrf4e"
    }
}