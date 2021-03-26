package com.llj.living.ui.activity

import android.net.Uri
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.custom.ext.baseObserver
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivityVideoPreviewBinding
import com.llj.living.logic.vm.VideoPreviewVM
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivityVideoPreview:BaseActivity<ActivityVideoPreviewBinding>() {

    override fun getLayoutId() = R.layout.activity_video_preview

    override fun showToolbar() = false

    private val viewModel by viewModels<VideoPreviewVM>()
    private lateinit var uri:Uri

    override fun init() {
        val tempUri = intent.getParcelableExtra<Uri>(ActivityVideotape.VIDEO_URI)

        if (tempUri==null){
            lifecycleScope.launch {
                ToastUtils.toastLong("未获取到视频路径 请返回重试")
                delay(2000)
                finish()
            }
            return
        }else uri = tempUri

        lifecycle.addObserver(viewModel.getPlayer())

        lifecycleScope.launch {//设置2s后自动隐藏控制界面
            delay(1500)
            getDataBinding().videoViewController.let {
                if (it.isVisible) it.visibility = View.INVISIBLE
            }
        }

        getDataBinding().apply {
            tvVideoName.text = uri.path.toString()
            root.post {
                playingVideo()
            }
            ivQuitPlaying.setOnClickListener {
                finish()
            }
            videoViewController.setOnClickListener {
                if (it.isVisible) it.visibility = View.INVISIBLE
                else it.visibility = View.VISIBLE
            }
            ivPlayerControl.setOnClickListener {
                val statusPicture = if (viewModel.getPlayer().isPlaying){
                    viewModel.getPlayer().resumePlayer()
                    R.drawable.ic_baseline_pause_circle_outline_24
                }else{
                    viewModel.getPlayer().pausePlayer()
                    R.drawable.ic_baseline_play_circle_outline_24
                }
                ivPlayerControl.setImageResource(statusPicture)
            }
            surfaceView.holder.addCallback(object :SurfaceHolder.Callback{
                override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int
                ) {
                    viewModel.getPlayer().apply {
                        setDisplay(holder)
                        setScreenOnWhilePlaying(true) //设置不熄灭屏幕
                    }
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {}

                override fun surfaceCreated(holder: SurfaceHolder) {}

            })
        }

        viewModel.apply {
            videoSizeLiveData.baseObserver(this@ActivityVideoPreview){
                resizePlayer(it.first,it.second)
            }
            currentPlayingTime.baseObserver(this@ActivityVideoPreview){
                getDataBinding().tvCurrentTime.text = it.toString()
                getDataBinding().seekBar.progress = it
            }
            allPlayingTime.baseObserver(this@ActivityVideoPreview){
                getDataBinding().tvVideoAllTime.text = it.toString()
                getDataBinding().seekBar.max = it
            }

        }
    }

    private fun playingVideo(){
        viewModel.loadVideo(uri)
    }

    private fun resizePlayer(width:Int,height:Int){
        if (width==0||height==0) return
        getDataBinding().surfaceView.apply {
            layoutParams = FrameLayout.LayoutParams(
                getDataBinding().VideoFrameLayout.height * width / height,
                FrameLayout.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
            )
        }
    }

}