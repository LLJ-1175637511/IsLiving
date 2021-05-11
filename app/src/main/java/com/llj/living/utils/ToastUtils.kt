package com.llj.living.utils

import android.widget.Toast

object ToastUtils {

    fun toastShort(msg:String){
        Toast.makeText(ContextUtils.getInstance(),msg,Toast.LENGTH_SHORT).show()
    }

    fun toastLong(msg:String){
        Toast.makeText(ContextUtils.getInstance(),msg,Toast.LENGTH_LONG).show()
    }
}