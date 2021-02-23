package com.llj.living.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen()
        setContentView(R.layout.activity_splash)
        lifecycleScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
            finish()
        }
    }

    private fun setFullScreen() {
        //沉浸式效果
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
}