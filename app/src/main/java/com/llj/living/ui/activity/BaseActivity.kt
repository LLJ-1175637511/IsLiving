package com.llj.living.ui.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.llj.living.R
import com.llj.living.databinding.ActivityBaseViewBinding
import com.llj.living.utils.ToastUtils

abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity() {

    abstract fun getLayoutId(): Int

    private var isFullScreen = false

    val TAG = this.javaClass.simpleName

    private lateinit var dataBinding: DB
    private lateinit var toolbar:Toolbar
    private lateinit var baseUi:FrameLayout
    private lateinit var linear:LinearLayout
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()

        initToolbar()

        init()

//        if (isFullScreen) setFullScreen()

    }

    private fun initToolbar() {
//        setSupportActionBar(toolbar)
        toolbar.apply {
//            inflateMenu(R.menu.toolbar_menu)
            setNavigationOnClickListener {
                ToastUtils.toastShort("back")
            }

            setOnMenuItemClickListener { item ->
                when(item.itemId){
                    R.id.add_item->{ ToastUtils.toastShort("add_item") }
                    R.id.remo_item->{ ToastUtils.toastShort("remo_item") }
                    R.id.more_item->{ ToastUtils.toastShort("more_item") }
                }
                false
            }
        }
    }

    private fun initView() {
        val baseView = DataBindingUtil.setContentView<ActivityBaseViewBinding>(this,R.layout.activity_base_view)
        baseView.root.apply{
            linear = findViewById<LinearLayout>(R.id.activity_base_linear)
            toolbar = findViewById<Toolbar>(R.id.toolbar_base)
            baseUi = findViewById<FrameLayout>(R.id.activity_base_content)
            dataBinding = DataBindingUtil.inflate<DB>(layoutInflater,getLayoutId(),baseUi as ViewGroup,false)
            baseUi.addView(dataBinding.root)
        }
    }

    open fun init(){}

    protected open fun hasToolBar(): Boolean {
        return true
    }

    private fun setFullScreen() {
        //沉浸式效果
        // LOLLIPOP解决方案
        window.statusBarColor = Color.TRANSPARENT//状态栏设置为透明色
        window.navigationBarColor = Color.TRANSPARENT
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.setDecorFitsSystemWindows(false)
//            window.insetsController?.let {
//                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
//                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            }
//        } else {
//            @Suppress("DEPRECATION")
//            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
//                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
//        }
    }

    fun getDataBinding() = dataBinding

    fun setIsFullScreen(bool: Boolean) {
        isFullScreen = bool
    }


}