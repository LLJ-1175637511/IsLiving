package com.llj.living.custom.ext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.llj.living.R
import com.llj.living.databinding.DialogBaseBinding
import com.llj.living.utils.LogUtils
import com.llj.living.utils.ToastUtils

const val TAG = "QUICK_VIEW"

fun <DB : ViewDataBinding> inflateDataBinding(
    parent: ViewGroup,
    @LayoutRes layoutId: Int
): DB = DataBindingUtil.inflate<DB>(
    LayoutInflater.from(parent.context),
    layoutId,
    parent,
    false
)


/**
 * 在主线程调用加载UI
 */
@MainThread
fun buildDialog(
    parent: ViewGroup,
    addView: View,
    errTips: String = "",
    block: () -> Unit
) {
    var ad: AlertDialog? = null
    try {
        val baseBinding = inflateDataBinding<DialogBaseBinding>(parent, R.layout.dialog_base)
        ad = AlertDialog.Builder(parent.context).setCancelable(false).create().apply {
            baseBinding.close.setOnClickListener {
                this.cancel()
            }
            setView(baseBinding.root)
        }
        ad.show()
        block()
        baseBinding.baseFL.addView(addView)
    } catch (e: Exception) {
        ToastUtils.toastShort("${errTips}加载失败")
        LogUtils.d(TAG, "AlertDialog:${e.message}")
        ad?.cancel()
    }
}

/**
 * 在主线程调用加载UI
 */
@MainThread
suspend fun buildCoroutineDialog(
    parent: ViewGroup,
    addView: View,
    errTips: String = "",
    block: suspend () -> Unit
) {
    var ad: AlertDialog? = null
    try {
        val baseBinding = inflateDataBinding<DialogBaseBinding>(parent, R.layout.dialog_base)
        ad = AlertDialog.Builder(parent.context).setCancelable(false).create().apply {
            baseBinding.close.setOnClickListener {
                this.cancel()
            }
            setView(baseBinding.root)
        }
        ad.show()
        block()
        baseBinding.baseFL.addView(addView)
    } catch (e: Exception) {
        ToastUtils.toastShort("${errTips}加载失败")
        LogUtils.d(TAG, "AlertDialog:${e.message}")
        ad?.cancel()
    }
}

fun AppCompatActivity.loadCircleView(url:String,imgView:ImageView){
    Glide.with(this).load(url)
        .apply(RequestOptions.bitmapTransform(CircleCrop()))
        .into(imgView)
}

fun AppCompatActivity.loadView(url:String,imgView:ImageView){
    Glide.with(this).load(url).into(imgView)
}

fun Fragment.loadCircleView(url:String,imgView:ImageView){
    Glide.with(this).load(url)
        .apply(RequestOptions.bitmapTransform(CircleCrop()))
        .into(imgView)
}

fun Fragment.loadView(url:String,imgView:ImageView){
    Glide.with(this).load(url).into(imgView)
}

