package com.llj.living.custom.ext

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.llj.living.data.bean.BaseBean
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.*
import java.lang.reflect.Type
import java.text.SimpleDateFormat

/*--------------------------------------- 公共 扩展函数 -------------------------------------------*/

/**
 * 便捷添加观察事件
 */
fun <T> LiveData<T>.baseObserver(lifecycleOwner: LifecycleOwner, block: (T) -> Unit) {
    this.observe(lifecycleOwner, Observer { block(it) })
}


/*----------------------------------- activity 扩展函数 -------------------------------------------*/

inline fun <reified T : AppCompatActivity> AppCompatActivity.startCommonFinishedActivity() {
    startActivity(Intent(this, T::class.java))
    this.finish()
}

inline fun <reified T : AppCompatActivity> AppCompatActivity.startCommonActivity() {
    startActivity(Intent(this, T::class.java))
}

fun AppCompatActivity.tryExceptionLaunch(
    thread: CoroutineDispatcher = Dispatchers.Default,
    block: suspend () -> Unit
): String? = try {
    commonLaunch(thread) {
        block()
    }
    null
} catch (e: Exception) {
    e.printStackTrace()
    LogUtils.d("${this.javaClass.simpleName}EXT:", e.message.toString())
    e.message
}

fun AppCompatActivity.commonLaunch(
    thread: CoroutineDispatcher = Dispatchers.Default,
    block: suspend () -> Unit
) = lifecycleScope.launch(thread) {
    block()
}

fun AppCompatActivity.commonTryException(
    block: () -> Unit
): Exception? = try {
    block()
    null
} catch (e: Exception) {
    e.printStackTrace()
    LogUtils.d(this.javaClass.simpleName, e.message.toString())
    e
}

fun AppCompatActivity.toastShort(msg:String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.toastLong(msg:String){
    Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
}
/*----------------------------------- viewModel 扩展函数 ------------------------------------------*/

fun ViewModel.tryExceptionLaunch(
    thread: CoroutineDispatcher = Dispatchers.Default,
    block: () -> Unit
): Exception? = try {
    viewModelScope.launch(thread) {
        block()
    }
    null
} catch (e: Exception) {
    e.printStackTrace()
    LogUtils.d(this.javaClass.simpleName, e.message.toString())
    e
}

suspend inline fun <reified T> baseBeanConverter(bb: BaseBean): T = withContext(Dispatchers.IO) {
    val gson = Gson()
    val type = object : TypeToken<T>() {}.type
    gson.fromJson(bb.result, type) as T
}

fun ViewModel.commonLaunch(
    thread: CoroutineDispatcher = Dispatchers.Default,
    block: suspend () -> Unit
) = viewModelScope.launch(thread) {
    block()
}

fun <T> ViewModel.commonAsych(
    thread: CoroutineDispatcher = Dispatchers.Default,
    block: suspend () -> T
) = viewModelScope.async(thread) {
    block()
}

fun ViewModel.commonTryException(
    block: () -> Unit
): Exception? = try {
    block()
    null
} catch (e: Exception) {
    e.printStackTrace()
    LogUtils.d(this.javaClass.simpleName, e.message.toString())
    e
}

/*-------------------------------------- 工具类 扩展函数 ------------------------------------------*/

/**
 * byte 转 base64
 * 建议子线程调用
 */
fun ByteArray.toBase64(): String = Base64.encodeToString(this, Base64.DEFAULT)

fun stringToBean(body: String, type: Type): Any = try {
    val gson = Gson()
    gson.fromJson(body, type)
} catch (e: Exception) {
    throw e
}

fun String.isMsgSuc(): Boolean = this == "success"

fun String.isCodeSuc(): Boolean = this == "000"

fun String.versionToInt(): Int?{
    if (this.contains('.')){
        val ls = this.split('.')
        if (ls.size==3){
            return ls[0].toInt()*100+ls[1].toInt()*10+ls[2].toInt()
        }
    }
    return null
}



fun Int.convertToTime() = (this % 60).toString().let {
    if (it.length == 1) {
        "00:0${it}"
    } else "00:${it}"
}

fun String.getVideoName(): String {
    return if (this.contains("/")) {
        val temp = this.split("/")
        temp[temp.lastIndex]
    } else this
}

@SuppressLint("SimpleDateFormat")
fun Long.toSimpleTime(): String = SimpleDateFormat("yyyy-MM-dd").format(this)
