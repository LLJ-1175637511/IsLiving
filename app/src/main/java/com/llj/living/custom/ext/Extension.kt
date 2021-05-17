package com.llj.living.custom.ext

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.llj.living.application.MyApplication
import com.llj.living.data.bean.BaseBaiduBean
import com.llj.living.data.bean.BaseBean
import com.llj.living.data.const.Const
import com.llj.living.utils.ContextUtils
import com.llj.living.utils.LogUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.*
import java.lang.reflect.Type
import java.text.SimpleDateFormat

const val EXT_TAG = "EXT_TAG"
/*--------------------------------------- 公共 扩展函数 -------------------------------------------*/

/**
 * 便捷添加观察事件
 */
fun <T> LiveData<T>.baseObserver(lifecycleOwner: LifecycleOwner, block: (T) -> Unit) {
    observe(lifecycleOwner, Observer { block(it) })
}

suspend inline fun <reified T> baseBeanConverter(bb: BaseBean): T = withContext(Dispatchers.IO) {
    val gson = Gson()
    val type = object : TypeToken<T>() {}.type
    gson.fromJson(bb.result, type) as T
}

suspend inline fun <reified T> baseBaiduBeanConverter(bb: BaseBaiduBean): T =
    withContext(Dispatchers.IO) {
        val gson = Gson()
        val type = object : TypeToken<T>() {}.type
        val result = bb.result
        gson.fromJson(result, type) as T
    }

fun CoroutineScope.tryExceptionLaunch(
    thread: CoroutineDispatcher = Dispatchers.Default,
    errTips: String = "",
    block: suspend () -> Unit
) = launch(thread) {
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
            ToastUtils.toastShort("${errTips}请求错误 请重试")
        }
        LogUtils.d("${this.javaClass.simpleName}EXC", "EXCEPTION:${e.message.toString()}")
    }
}

suspend fun <T> tryExceptionAsych(
    thread: CoroutineDispatcher = Dispatchers.Default,
    errTips: String = "",
    block: suspend () -> T?
) = withContext(thread) {
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        ToastUtils.toastShort("${errTips}请求错误 请重试")
        LogUtils.d("${this.javaClass.simpleName}EXC", "EXCEPTION:${e.message.toString()}")
        null
    }
}


suspend inline fun <reified T> quickRequest(
    crossinline block: suspend () -> BaseBean
): T? = withContext(Dispatchers.Default) {
    val beanPair = realRequest<T> {
        block()
    }
    val exception = beanPair.second
    if (exception != null) {
        withContext(Dispatchers.Main) {
            ToastUtils.toastShort(exception.message.toString())
        }
        return@withContext null
    }
    beanPair.first
}

/**
 * token:带有token的网络请求
 */
suspend inline fun <reified T> quickTokenRequest(
    crossinline block: suspend (String) -> BaseBean
): T? = withContext(Dispatchers.Default) {
    val token =
        ContextUtils.getInstance().getSP(Const.SPUser).getString(Const.SPUserTokenLogin, "")
    if (token.isNullOrEmpty()) {
        ToastUtils.toastShort("token获取失败")
        return@withContext null
    }
    val beanPair = realRequest<T> {
        block(token)
    }
    val exception = beanPair.second
    if (exception != null) {
        if (exception.message == "tokenErr") {
            MyApplication.setTokenInvalid(true)
        } else {
            withContext(Dispatchers.Main) {
                ToastUtils.toastShort(exception.message.toString())
            }
        }
        return@withContext null
    }
    beanPair.first
}

suspend inline fun <reified T> realRequest(crossinline block: suspend () -> BaseBean): Pair<T?, Exception?> {
    var exc: Exception? = null
    var data: T? = null
    try {
        val baseBean = block()
        LogUtils.d(EXT_TAG, baseBean.toString())
        if (baseBean.code.isCodeSuc() && baseBean.msg.isMsgSuc()) {
            data = baseBeanConverter<T>(baseBean)
            val sp = ContextUtils.getInstance().getSP(Const.SPMySqlNet)
            val token = baseBean.token
            if (token.isNotEmpty()) {
                sp.save {
                    putString(Const.SPMySqlToken, token)
                }
            }
        } else if (baseBean.code.isTokenInvalid()) {
            exc = Exception("tokenErr")

        } else {
            exc = Exception("错误--> errCode:${baseBean.code} errMsg:${baseBean.msg}")
        }
    } catch (e: Exception) {
        LogUtils.d(EXT_TAG, "EXCEPTION:${e.message}")
        exc = e
        e.printStackTrace()
    }
    return Pair(data, exc)
}


/*----------------------------------- activity 扩展函数 -------------------------------------------*/

inline fun <reified T : AppCompatActivity> AppCompatActivity.startCommonFinishedActivity() {
    startActivity(Intent(this, T::class.java))
    this.finish()
}

inline fun <reified T : AppCompatActivity> AppCompatActivity.startCommonFinishedActivity(i: Intent) {
    startActivity(i)
    this.finish()
}

inline fun <reified T : AppCompatActivity> AppCompatActivity.startCommonActivity() {
    startActivity(Intent(this, T::class.java))
}

inline fun <reified T : AppCompatActivity> AppCompatActivity.startCommonActivity(i: Intent) {
    startActivity(i)
}

fun AppCompatActivity.tryException(
    errTips: String = "",
    block: () -> Unit
) = try {
    block()
} catch (e: Exception) {
    e.printStackTrace()
    runOnUiThread {
        toastShort("${errTips}请求错误 请重试")
    }
    LogUtils.d("${this.javaClass.simpleName}EXC", "EXCEPTION:${e.message.toString()}")
}

fun AppCompatActivity.commonLaunch(
    thread: CoroutineDispatcher = Dispatchers.Default,
    block: suspend () -> Unit
) = lifecycleScope.launch(thread) {
    block()
}

fun <T> AppCompatActivity.commonAsych(
    thread: CoroutineDispatcher = Dispatchers.Default,
    block: suspend () -> T
) = lifecycleScope.async<T>(thread) {
    block()
}

fun AppCompatActivity.tryExceptionLaunch(
    thread: CoroutineDispatcher = Dispatchers.Default,
    errTips: String = "",
    block: suspend () -> Unit
) = lifecycleScope.launch(thread) {
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
            ToastUtils.toastShort("${errTips}请求错误 请重试")
        }
        LogUtils.d("${this.javaClass.simpleName}_EXC", "EXCEPTION:${e.message.toString()}")
    }
}


fun AppCompatActivity.toastShort(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.toastLong(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}
/*----------------------------------- viewModel 扩展函数 ------------------------------------------*/

fun ViewModel.commonLaunch(
    thread: CoroutineDispatcher = Dispatchers.Default,
    block: suspend () -> Unit
) = this.viewModelScope.launch(thread) {
    block()
}

fun ViewModel.tryExceptionLaunch(
    thread: CoroutineDispatcher = Dispatchers.Default,
    errTips: String = "",
    block: suspend () -> Unit
) = viewModelScope.launch(thread) {
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
            ToastUtils.toastShort("${errTips}请求错误 请重试")
        }
        LogUtils.d("${this.javaClass.simpleName}_EXC", "EXCEPTION:${e.message.toString()}")
    }
}

fun <T> ViewModel.commonAsych(
    thread: CoroutineDispatcher = Dispatchers.Default,
    block: suspend () -> T
) = viewModelScope.async<T>(thread) {
    block()
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

fun String.isBaiduMsgSuc(): Boolean = this == "SUCCESS"

fun String.isCodeSuc(): Boolean = this == "000"

fun String.isTokenInvalid(): Boolean = this == "010"

fun Int.isBaiduCodeSuc(): Boolean = this == 0

fun String.versionToInt(): Int? {
    if (this.contains('.')) {
        val ls = this.split('.')
        if (ls.size == 3) {
            return ls[0].toInt() * 100 + ls[1].toInt() * 10 + ls[2].toInt()
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
fun Long.toSimpleTime(): String = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(this)
