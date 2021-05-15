package com.llj.living.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.llj.living.R
import com.llj.living.custom.ext.baseObserver
import com.llj.living.custom.view.NetDialog
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ToolbarBaseBinding
import com.llj.living.utils.ToastUtils

abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity() {

    @LayoutRes
    abstract fun getLayoutId(): Int

    val TAG = "${this.javaClass.simpleName}BASE"

    private lateinit var mDataBinding: DB

    private var toolbarDB: ToolbarBaseBinding? = null

    private val mToolbarConfig by lazy { ToolbarConfig() }

    private val netLiveData = MutableLiveData<Boolean>()

    private val dialog by lazy { NetDialog(this) }

    open fun init() {}

    open fun showToolbar(): Boolean = true

    open fun isScreenFull(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val filter = IntentFilter().apply {
                addAction("android.net.conn.CONNECTIVITY_CHANGE")
            }
            registerReceiver(myNetworkChangeReceiver, filter)
            initView()
        }catch (e:Exception){
            e.printStackTrace()
            ToastUtils.toastShort("初始化错误")
            finish()
        }
    }

    protected fun getToolbar() = toolbarDB?.toolbarBase

    protected fun getDataBinding() = mDataBinding

    /**
     * 初始化toolbar参数
     */
    fun setToolbar(toolbarConfig: ToolbarConfig) {
        mToolbarConfig.apply {
            title = toolbarConfig.title
            isShowBack = toolbarConfig.isShowBack
            isShowMenu = toolbarConfig.isShowMenu
        }
    }

    private fun initView() {
        mDataBinding = DataBindingUtil.setContentView<DB>(this, getLayoutId())
        if (showToolbar()) { //是否初始化toolbar
            toolbarDB =
                DataBindingUtil.findBinding(mDataBinding.root.findViewById<Toolbar>(R.id.toolbar))
            initToolbar()
        }
        mDataBinding.lifecycleOwner = this@BaseActivity
        netLiveData.baseObserver(this) {
            if (!it) dialog.show()
            else {
                dialog.cancel()
                init()
            }
        }
    }

    private fun initToolbar() {
        toolbarDB?.let {
            title = mToolbarConfig.title
            it.toolbarBase.apply {
                if (mToolbarConfig.isShowMenu) {
                    inflateMenu(R.menu.toolbar_menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.quit_user -> {
                                val i = Intent(this@BaseActivity, LoginActivity::class.java)
                                startActivity(i)
                                finish()
                            }
                            R.id.more_takePhoto -> {
                                val i = Intent(this@BaseActivity, FaceAuthenticActivity::class.java)
                                startActivity(i)
                            }
                        }
                        false
                    }
                }
                if (mToolbarConfig.isShowBack) {
                    setNavigationOnClickListener {
                        finish()
                    }
                } else {
                    navigationIcon = null
                }
            }
        }
    }

    /**
     * 注册网路状态广播监听
     */
    private val myNetworkChangeReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceive(context: Context?, intent: Intent?) {
            // 获取管理网络连接的系统服务类的实例
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            netLiveData.postValue(cm.activeNetwork != null)
        }
    }

    /**
     * 设置全屏
     */
    private fun setFullScreen() {
        if (!isScreenFull()) return
        //沉浸式效果
        window.statusBarColor = Color.TRANSPARENT//状态栏设置为透明色
        window.navigationBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val option = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
            window.decorView.systemUiVisibility = option
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(android.R.color.transparent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //界面销毁后 解绑dataBinding
        if (this::mDataBinding.isInitialized) mDataBinding.unbind()
        unregisterReceiver(myNetworkChangeReceiver)
    }
}
