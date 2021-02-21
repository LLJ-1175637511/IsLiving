package com.llj.living.utils

import android.content.Context
import android.widget.Toast

object ToastUtils {

    private lateinit var context: Context

    fun init(context: Context){
        this.context = context
    }

    fun toastShort(msg:String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }

    fun toastLong(msg:String){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show()
    }
}