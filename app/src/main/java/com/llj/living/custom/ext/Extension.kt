package com.llj.living.custom.ext

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.llj.living.LoginActivity


/**
 * 便捷添加观察事件
 */
fun <T> LiveData<T>.baseObserver(lifecycleOwner: LifecycleOwner, block: (T)->Unit){
    this.observe(lifecycleOwner, Observer { block(it) })
}