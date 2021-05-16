package com.llj.living.ui.activity

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.iceteck.silicompressorr.SiliCompressor
import com.llj.living.R
import com.llj.living.custom.ext.*
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.data.const.Const
import com.llj.living.databinding.ActivityVideotapeBinding
import com.llj.living.logic.vm.VideoTapeVM
import com.llj.living.utils.LogUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ActivityVideotape : BaseTPActivity<ActivityVideotapeBinding>() {

    override fun getLayoutId() = R.layout.activity_videotape

    override fun setToolbar() = ToolbarConfig("视频核查", isShowBack = true, isShowMenu = false)

    private var realpath = "compressedVideo"
    private var peopleId: Int = -1
    private var checkId: Int = -1

    private lateinit var uri: Uri

    private val videoTapeVM by viewModels<VideoTapeVM>()

    override fun init() {

        peopleId = intent.getIntExtra(INTENT_ID_CHECK_FLAG, -1)
        checkId = getSP(Const.SPCheck).getInt(Const.SPCheckCheckId, -1)

        if (peopleId == -1 || checkId == -1) {
            ToastUtils.toastShort("数据解析错误 请返回重试")
            finish()
            return
        }

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
                launchOldManVideo.launch(getVideoIntent(path))
            }
        }

        getDataBinding().btMakeSure.setOnClickListener {
            lifecycleScope.launch {
                uploadVideo()
            }
        }
    }

    private suspend fun uploadVideo() {
        buildActivityCoroutineDialog(layoutInflater, null) { bd, ld ->
            try {
                bd.close.text = "取消"
                bd.tvTipsStr.text = "视频文件压缩中"
                val tempPath = File(this.externalCacheDir, "VID_${path}.mp4").absolutePath
                tempPath ?: throw Exception("路径解析错误")
                LogUtils.d("${TAG}_TT", "tempPath:${tempPath}")
                val newPath = withContext(Dispatchers.IO) {
                    val newFile = File(this@ActivityVideotape.externalCacheDir, realpath)
                    if (!newFile.exists()) {
                        newFile.mkdir()
                    }
                    val externalPath = newFile.absolutePath
                    externalPath ?: throw Exception("路径解析错误")
                    SiliCompressor.with(this@ActivityVideotape)
                        .compressVideo(tempPath, externalPath)
                }
                bd.tvTipsStr.text = "文件上传中"
                LogUtils.d(TAG, "newPath:${newPath}")
                val result = videoTapeVM.uploadVideo(checkId, peopleId, newPath)

                LogUtils.d(TAG, result.toString())

                if (result.code.isCodeSuc() && result.msg.isMsgSuc()) {
                    ToastUtils.toastShort("上传成功")
                    ld?.cancel()
                    delay(1000)
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ld?.cancel()
            }
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
    private val launchOldManVideo by lazy {
        buildVideoLaunch { videoUri ->
            videoUri?.let {
                uri = it
                LogUtils.d(TAG, "path:${uri.path}")
                getDataBinding().apply {
                    tvVideoSavedTips.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        const val path = "tempVideo"

        const val VIDEO_URI = "checkVideoUri"
        const val PREVIEW_ORIENTATION_VERTICAL = "preview_orientation_vertical"

        const val INTENT_ID_CHECK_FLAG = "intent_id_check_video_flag"
        const val INTENT_BEAN_CHECK_FLAG = "intent_bean_check_video_flag"
    }
}