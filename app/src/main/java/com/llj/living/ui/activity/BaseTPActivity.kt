package com.llj.living.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.llj.living.data.enums.TakePhotoEnum
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

abstract class BaseTPActivity<DB : ViewDataBinding> : BaseActivity<DB>() {

    private val ops = BitmapFactory.Options()

    private var bitmap: Bitmap? = null

    private var imageUri: Uri? = null
    private var videoUri: Uri? = null

    private val _proportion = MutableLiveData<Float>()
    val proportion: LiveData<Float> = _proportion

    init {
        ops.apply {
            inSampleSize = 6 //设置缩放比例（必须为2的倍数）
            inPremultiplied = true //设置可回收
//            inPreferredConfig = Bitmap.Config.ARGB_8888 //设置编码方式 普通一像素2字节 默认为当前4字节
            inPreferredConfig = Bitmap.Config.ALPHA_8 //设置编码方式 普通一像素2字节 默认为当前4字节
        }
    }

    /**
     * type:1、ONLY_FACE_TYPE 2、OTHER_TYPE
     */
    suspend fun getPhotoIntent(type: TakePhotoEnum): Intent {
        buildUri(type)
        LogUtils.d(TAG, "imageUri is null:${imageUri == null}")
        return Intent("android.media.action.IMAGE_CAPTURE").apply {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        }
    }

    suspend fun getVideoIntent(path: String): Intent {
        buildUri("VID_${path}.mp4")
        LogUtils.d(TAG, "videoUri is null:${videoUri == null}")
        return Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
            putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
            putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15)
        }
    }

    /**
     * build imageUri
     */
    private suspend fun buildUri(type: TakePhotoEnum) =
        withContext(lifecycleScope.coroutineContext + Dispatchers.IO) {
            val path = when (type) {
                TakePhotoEnum.PersonFace -> "${TakePhotoEnum.PersonFace.name}.jpg"
                TakePhotoEnum.IdcardFront -> "${TakePhotoEnum.IdcardFront.name}.jpg"
                TakePhotoEnum.IdcardBehind -> "${TakePhotoEnum.IdcardBehind.name}.jpg"
            }
            val outputImageFile =
                File(externalCacheDir, path) //设置图片路径 包含文件后缀 xx.jpg
            try {
                if (outputImageFile.exists()) outputImageFile.delete()
                val result = outputImageFile.createNewFile()
                if (result) {
                    imageUri = if (Build.VERSION.SDK_INT < 24) {
                        Uri.fromFile(outputImageFile)
                    } else {
                        FileProvider.getUriForFile(
                            this@BaseTPActivity,
                            "com.llj.living.fileprovider",//定义唯一标识，关联后面的内容提供器
                            outputImageFile
                        )
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                LogUtils.d(TAG, "file create failed")
            }
        }

    /**
     * build videoUri
     */
    private suspend fun buildUri(videoPath: String) =
        withContext(lifecycleScope.coroutineContext + Dispatchers.IO) {
            val outputVideoFile =
                File(externalCacheDir, videoPath) //设置图片路径 包含文件后缀 xx.mp4
            try {
                if (outputVideoFile.exists()) outputVideoFile.delete()
                val result = outputVideoFile.createNewFile()
                if (result) {
                    videoUri = if (Build.VERSION.SDK_INT < 24) {
                        Uri.fromFile(outputVideoFile)
                    } else {
                        FileProvider.getUriForFile(
                            this@BaseTPActivity,
                            "com.llj.living.fileprovider",//定义唯一标识，关联后面的内容提供器
                            outputVideoFile
                        )
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                LogUtils.d(TAG, "file create failed")
            }
        }

    fun buildLaunch(block: (bitmap: Bitmap?) -> Unit) =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            try {
                imageUri?.let { uri ->
                    bitmap =
                        BitmapFactory.decodeStream(contentResolver.openInputStream(uri), null, ops)
                    bitmap?.let {
                        _proportion.postValue(it.height.toFloat()/it.width)
                    }
                    block(bitmap)
                }
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
                LogUtils.d(TAG, "图片过大 内存溢出")
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.d(TAG, e.message.toString())
            }
//            base64 = PhotoUtils.bitmapToBase64(bitmap).toString()
        }

    fun buildVideoLaunch(block: (uri: Uri) -> Unit) =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            try {
                videoUri?.let {
                    block(it)
                }
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
                LogUtils.d(TAG, "资源占用过大 内存溢出")
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.d(TAG, e.message.toString())
            }
//            base64 = PhotoUtils.bitmapToBase64(bitmap).toString()
        }

    override fun onDestroy() {
        super.onDestroy()
        bitmap?.let {
            if (!it.isRecycled) {
                it.recycle()
            }
        }
        if (imageUri != null) imageUri = null
        if (videoUri != null) videoUri = null
    }
}