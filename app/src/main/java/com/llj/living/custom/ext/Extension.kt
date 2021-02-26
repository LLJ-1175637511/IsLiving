package com.llj.living.custom.ext

import android.util.Base64
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import java.lang.reflect.Type


/**
 * 便捷添加观察事件
 */
fun <T> LiveData<T>.baseObserver(lifecycleOwner: LifecycleOwner, block: (T)->Unit){
    this.observe(lifecycleOwner, Observer { block(it) })
}

/**
 * byte 转 base64
 * 建议子线程调用
 */
fun ByteArray.toBase64():String = Base64.encodeToString(this,Base64.DEFAULT)

fun stringToBean(body: String, type: Type): Any = try {
    val gson = Gson()
    gson.fromJson(body, type)
} catch (e: Exception) {
    throw e
}

fun String.isMsgSuc():Boolean = this=="SUCCESS"

fun Int.isCodeSuc():Boolean = this==0