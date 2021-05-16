package com.llj.living.utils

import android.util.Log

object LogUtils {
    const val FLAG = false
    fun d(tag: String, msg: String) {
        if (FLAG) Log.d(tag, msg)
    }
}