package com.llj.living.ui.activity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.custom.ext.baseObserver
import com.llj.living.custom.ext.convertToTime
import com.llj.living.custom.ext.getVideoName
import com.llj.living.custom.ext.toastLong
import com.llj.living.databinding.ActivityVideoPreviewBinding
import com.llj.living.logic.vm.VideoPreviewVM
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivityVideoPreview : AppCompatActivity() {

    private val viewModel by viewModels<VideoPreviewVM>()

    private lateinit var uri: Uri
    private var userOperasTime = 0

    private lateinit var binding: ActivityVideoPreviewBinding

    private var isVertical = false

    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {

        hideSystemUi() //隐藏系统顶部状态栏
        initOrientation() //初始化屏幕方向

        super.onCreate(savedInstanceState)

        val tempUri = intent.getParcelableExtra<Uri>(ActivityVideotape.VIDEO_URI)
        if (tempUri == null) {
            finish()
            return
        } else uri = tempUri

        initUi()
        initVM()
        initListener()
        initOther()
    }

    private fun initOrientation() {
        isVertical = intent.getBooleanExtra(ActivityVideotape.PREVIEW_ORIENTATION_VERTICAL, false)
        if (!isVertical) { //如果是竖屏录制的视频 则垂直播放
            if (resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }
    }

    private fun initUi() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_preview)
        binding.apply {
            tvVideoName.text = uri.path.toString().getVideoName()
            surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int
                ) {
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {}
                override fun surfaceCreated(holder: SurfaceHolder) {
                    viewModel.getPlayer().apply {
                        setDisplay(holder)
                        setScreenOnWhilePlaying(true) //设置不熄灭屏幕
                    }
                }
            })
        }
    }

    private fun initVM() {
        viewModel.apply {
            videoSizeLiveData.baseObserver(this@ActivityVideoPreview) {
                if (it.first == 0 || it.second == 0) return@baseObserver
                binding.VideoFrameLayout.post {
                    resizePlayer(it.first, it.second)
                }
            }
            currentPlayingTime.baseObserver(this@ActivityVideoPreview) {
                val temp = it / 1000
                binding.tvCurrentTime.text = temp.convertToTime()
                binding.seekBar.progress = temp
                if (temp >= binding.seekBar.max) {
                    LogUtils.d("ActivityVideoPreview", "finished")
                    lifecycleScope.launch {
                        delay(1000)
                        finish()
                    }
                }
            }
            allPlayingTime.baseObserver(this@ActivityVideoPreview) {
                binding.tvVideoAllTime.text = (it / 1000).convertToTime()
                binding.seekBar.max = it / 1000
            }
            isPrepared.baseObserver(this@ActivityVideoPreview) {
                binding.pbIsPrepared.visibility = it
            }
            loadVideo(uri)
        }
    }

    private fun initOther() {
        lifecycleScope.launch {//设置2s后自动隐藏控制界面
            while (true) {
                delay(1000)
                userOperasTime++
                if (userOperasTime > 3) { //3s用户没有操作 自动隐藏控制面板
                    binding.videoViewController.let {
                        if (it.isVisible) it.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun initListener() {
        lifecycle.addObserver(viewModel.getPlayer())
        binding.apply {
            ivQuitPlaying.setOnClickListener {
                finish()
            }

            VideoFrameLayout.setOnClickListener { _ ->
                videoViewController.let {
                    userOperasTime = 0
                    if (it.isVisible) it.visibility = View.INVISIBLE
                    else it.visibility = View.VISIBLE
                }
            }
            ivPlayerControl.setOnClickListener {
                userOperasTime = 0
                val statusPicture = if (!viewModel.getPlayer().isPlaying) {
                    viewModel.getPlayer().resumePlayer()
                    R.drawable.ic_baseline_pause_circle_outline_24
                } else {
                    viewModel.getPlayer().pausePlayer()
                    R.drawable.ic_baseline_play_circle_outline_24
                }
                ivPlayerControl.setImageResource(statusPicture)
            }
            binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                private var newProcess = 0
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) userOperasTime = 0
                    newProcess = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    viewModel.changeCurrentProgress(newProcess)
                }
            })
        }
    }

    private fun resizePlayer(width: Int, height: Int) {
        LogUtils.d(TAG, "width:${width} height:${height}")
        binding.surfaceView.apply {
            layoutParams = if (isVertical) {
                FrameLayout.LayoutParams(
                    binding.VideoFrameLayout.height * width / height,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER
                )
            } else {
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    binding.VideoFrameLayout.width * height / width,
                    Gravity.CENTER
                )
            }
        }
    }

    private fun hideSystemUi() {
        try {
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
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

}