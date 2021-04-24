package com.llj.living.custom.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import com.llj.living.R

class NetDialog : AlertDialog {

    constructor(context: Context?) : super(context)

    constructor(
        context: Context?,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    constructor(context: Context?, themeResId: Int) : super(context, themeResId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_netoff)
        setCancelable(false)
    }



    /* val netBinding = DataBindingUtil.inflate<ViewNetoffBinding>(
             layoutInflater,
             R.layout.view_netoff,
             mDataBinding.root as ViewGroup,
             false
         )
         AlertDialog.Builder(this).apply {
             setView(netBinding.root)
             netBinding.tvYes.setOnClickListener {
                 //网络正常时 获取网络不为null
                 if (netLiveData.value == true) { //如果之前是处于网络错误状态
                     init() //则执行init 初始化
                 }
             }
             netBinding.tvQuit.setOnClickListener {
                 finish()
             }
             netBinding.ivLoading.show()
             setCancelable(false)
         }.create()*/
}
