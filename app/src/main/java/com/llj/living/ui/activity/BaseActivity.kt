package com.llj.living.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.llj.living.R
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivityBaseViewBinding
import com.llj.living.utils.ToastUtils

abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity() {

    abstract fun getLayoutId(): Int

    private val TAG = this.javaClass.simpleName

    private lateinit var mDataBinding: DB

    private var mToolbar: Toolbar? = null

    private val mToolbarConfig by lazy { ToolbarConfig() }

    open fun init() {}

    open fun showToolbar(): Boolean = true

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        init()
        initToolbar()
    }

    fun getToolbar() = mToolbar

    fun getDataBinding() = mDataBinding

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

    /**
     * 便捷初始化viewModel
     */
    fun <VM:AndroidViewModel> initViewModel(vm: Class<VM>) = ViewModelProvider(this, SavedStateViewModelFactory(application, this))[vm]

    inline fun <reified VM:AndroidViewModel> initViewModel() = initViewModel(VM::class.java)

    fun <T:Activity> startCommonActivity(activity:Class<T>){
        startActivity(Intent(this,activity))
    }

    inline fun <reified T:Activity> startCommonActivity(){
        startCommonActivity(T::class.java)
    }

    private fun initView() {
        val baseView = DataBindingUtil.setContentView<ActivityBaseViewBinding>(
            this,
            R.layout.activity_base_view
        )
        baseView.root.apply {
            if (showToolbar()){
                mToolbar = findViewById<Toolbar>(R.id.toolbar_base)
                mToolbar?.visibility = View.VISIBLE
            } //是否初始化toolbar
            val baseUi = findViewById<FrameLayout>(R.id.activity_base_content)
            mDataBinding = DataBindingUtil.inflate<DB>(
                layoutInflater,
                getLayoutId(),
                baseUi as ViewGroup,
                false
            )
            baseUi.addView(mDataBinding.root)
            mDataBinding.lifecycleOwner = this@BaseActivity //初始化生命周期
        }
    }

    private fun initToolbar() {
        mToolbar?.apply {
            mToolbarConfig.let {
                val tvTitle = findViewById<TextView>(R.id.toolbar_base_title)
                tvTitle.text = it.title //设置标题
                if (it.isShowMenu) {//是否加载menu
                    inflateMenu(R.menu.toolbar_menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.add_item -> {
                                ToastUtils.toastShort("add_item")
                            }
                            R.id.remo_item -> {
                                ToastUtils.toastShort("remo_item")
                            }
                            R.id.more_item -> {
                                ToastUtils.toastShort("more_item")
                            }
                        }
                        false
                    }
                }

                navigationIcon = if (!it.isShowBack) null // 是否显示back
                else {
                    setNavigationOnClickListener {
                        ToastUtils.toastShort("back")
                    }
                    ContextCompat.getDrawable(
                        this@BaseActivity,
                        R.drawable.ic_baseline_arrow_back_24
                    )
                }
            }
        }
    }

}