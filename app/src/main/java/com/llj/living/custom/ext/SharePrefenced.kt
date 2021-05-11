package com.llj.living.custom.ext

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel

/**
 * 仿kotlin-ktx库 利用高阶函数建立一个save扩展函数 简化sp的使用
 */
fun SharedPreferences.save(block: SharedPreferences.Editor.() -> Unit): Boolean {
    val edit = edit()
//    edit.block()
    block(edit)
    return edit.commit()
}

fun AndroidViewModel.getSP(key: String) =
    getApplication<Application>().getSharedPreferences(key, Context.MODE_PRIVATE)

fun AppCompatActivity.getSP(key: String) =
    getSharedPreferences(key, Context.MODE_PRIVATE)

fun Context.getSP(key: String) =
    getSharedPreferences(key, Context.MODE_PRIVATE)
