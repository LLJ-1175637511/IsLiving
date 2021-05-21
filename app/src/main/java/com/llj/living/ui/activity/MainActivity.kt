package com.llj.living.ui.activity

import androidx.activity.viewModels
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.llj.living.R
import com.llj.living.custom.ext.*
import com.llj.living.data.bean.LoginBean
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivityNavMainBinding
import com.llj.living.logic.vm.DatabaseTestVM
import kotlinx.coroutines.delay

class MainActivity : BaseActivity<ActivityNavMainBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_nav_main

    override fun setToolbar() =  ToolbarConfig(
        title = resources.getString(R.string.app_name),
        isShowBack = false,
        isShowMenu = true
    )

    private val mainViewModel by viewModels<DatabaseTestVM>()

    override fun init() {
        buildBottomView()
        loadLoginBean()
        mainViewModel.isAllowUseByLocation.baseObserver(this){
            if (it==null){
                commonLaunch {
                    delay(500)
                    startCommonFinishedActivity<LoginActivity>()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.checkMainLocation()
    }

    private fun loadLoginBean() {
        val lb = intent.getParcelableExtra<LoginBean>(LoginActivity.ENT_BEAN_FLAG)
        if (lb == null) {
            toastShort("数据获取失败")
            commonLaunch {
                delay(1000)
                startCommonFinishedActivity<LoginActivity>()
            }
            return
        }
        mainViewModel.setEntBean(lb)
        getDataBinding().viewHeader.apply {
            lifecycleOwner = this@MainActivity
            vm = mainViewModel
            tryException(errTips = "头像") {
                //加载网络圆形图片
                loadCircleView(lb.avatar,getDataBinding().viewHeader.ivLogoMain)
            }
        }
    }

    private fun buildBottomView() {
        val navController = Navigation.findNavController(this, R.id.nav_main_fragment)
        val bottomBar: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        bottomBar.setupWithNavController(navController)
    }

}