package com.llj.living.ui.activity

import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.llj.living.R
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivityNavMainBinding

class MainActivity : BaseActivity<ActivityNavMainBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_nav_main

    private val TAG = this.javaClass.simpleName

    override fun init() {
        buildToolbar()
        buildBottomView()
        initListener()
    }

    private fun buildBottomView() {
        val navController = Navigation.findNavController(this,R.id.nav_main_fragment)
        val bottomBar:BottomNavigationView = findViewById(R.id.bottom_nav_view)
        bottomBar.setupWithNavController(navController)
    }

    private fun initListener() {
      /*  getDataBinding().btStartToFaceAuth.setOnClickListener {
            startCommonActivity<FaceAuthenticActivity>()
        }*/

    }

    private fun buildToolbar() {
        setToolbar(ToolbarConfig(title = resources.getString(R.string.app_name), isShowBack = false, isShowMenu = true))
//        getToolbar()?.findViewById<ImageView>(R.id.toolbar_base_img)?.let { //加载顶部logo
//            Glide.with(this)
//                .load(R.mipmap.logo_main)
//                .apply(RequestOptions.bitmapTransform(CircleCrop()))
//                .into(it)
//        }
    }
}