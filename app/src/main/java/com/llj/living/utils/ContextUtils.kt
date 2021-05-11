package com.llj.living.utils

import android.content.Context

object ContextUtils {

    private lateinit var context: Context

    fun init(context: Context){
        this.context = context
    }

    fun getInstance() = context
}