package com.llj.living.ui.activity

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.custom.ext.toastShort
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.data.database.OldManInfoWait
import com.llj.living.databinding.ActivityVideotapeBinding
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.ui.adapter.CheckDoingAdapter
import com.llj.living.ui.adapter.WaitCheckAdapter
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivityVideotape : BaseTPActivity<ActivityVideotapeBinding>() {

    override fun getLayoutId() = R.layout.activity_videotape

    private var path = ""
    private lateinit var uri: Uri
    private var bean: OldManInfoWait? = null
    private val dbViewModel by viewModels<DatabaseVM>()

    override fun init() {
        setToolbar(ToolbarConfig("视频核查", isShowBack = true, isShowMenu = false))
        path = intent.getStringExtra(WaitCheckAdapter.VIDEO_PATH_ID).toString()
        getDataBinding().btPreview.setOnClickListener {
            if (getDataBinding().tvVideoSavedTips.visibility == View.INVISIBLE) {
                toastShort("请先录制视频")
                return@setOnClickListener
            }
            val isVerticalPreview = getNextActivityOrientationIsVertical()
            if (isVerticalPreview == null) {
                toastShort("视频参数获取失败")
                return@setOnClickListener
            }
            startActivity(Intent(this, ActivityVideoPreview::class.java).apply {
                putExtra(VIDEO_URI, uri)
                putExtra(PREVIEW_ORIENTATION_VERTICAL, isVerticalPreview)
            })
        }

        getDataBinding().ivTakeVideo.setOnClickListener {
            lifecycleScope.launch {
                launchOldManPhoto.launch(getVideoIntent(path))
            }
        }

        val id = WaitCheckAdapter.id
        dbViewModel.getOldManInfoById(id).observe(this, Observer { data ->
            data?.let {
                bean = it
            }
        })

        getDataBinding().btMakeSure.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                updateData()
                delay(500)
                finish()
            }
        }
    }

    private fun updateData() {
        bean?.let {
            dbViewModel.finishedOneHad(it, CheckDoingAdapter.id)
        }
    }

    private fun getNextActivityOrientationIsVertical(): Boolean? {
        val retriever = MediaMetadataRetriever().apply {
            setDataSource(this@ActivityVideotape, uri)
        }
        val width =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH) //宽
        val height =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT) //高
        val rotation =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)//视频的方向角度
//                val  duration = Long.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) * 1000;//视频的长度
        LogUtils.d("ActivityVideotape", "width:${width} height:${height} rotation:${rotation}")
        if (width == null || height == null || rotation == null) {
            return null
        }
        return rotation == "90" || rotation == "270"
    }

    /**
     * 构建照相启动模式
     */
    private val launchOldManPhoto by lazy {
        buildVideoLaunch {
            uri = it
            getDataBinding().apply {
                tvVideoSavedTips.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val VIDEO_URI = "checkVideoUri"
        const val PREVIEW_ORIENTATION_VERTICAL = "preview_orientation_vertical"
    }
}