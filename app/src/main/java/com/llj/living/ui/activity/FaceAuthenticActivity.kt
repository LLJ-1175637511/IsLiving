package com.llj.living.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.data.enums.ActionType
import com.llj.living.data.enums.ImageType
import com.llj.living.databinding.ActivityFaceAuthBinding
import com.llj.living.logic.vm.FaceAuthViewModel
import com.llj.living.net.config.NetConfig
import com.llj.living.utils.LogUtils
import com.llj.living.utils.PhotoUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class FaceAuthenticActivity : BaseActivity<ActivityFaceAuthBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_face_auth

    private lateinit var viewMode: FaceAuthViewModel
    private var bitmap: Bitmap? = null
    private val TAG = this.javaClass.simpleName

    private val mLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
            bitmap?.let {
                val scaleValue = 800 //设置图片上限800kb
                val newPhoto = getBitmapFromScale()
                LogUtils.d(TAG, "newPhoto:${newPhoto == null}")
                if (newPhoto == null) return@let
                getDataBinding().ivFaceImgFaceAuth.setImageBitmap(newPhoto)
                LogUtils.d(TAG, "set photo finish")
                base64 = PhotoUtils.bitmapToBase64(newPhoto).toString()
                LogUtils.d(TAG, "base64 size:${base64.length}")
                // 用完了记得回收
//                if (!newPhoto.isRecycled) newPhoto.recycle()
                LogUtils.d(TAG, "all finished")
            }

        }

    private var base64 = ""

    private lateinit var imageUri: Uri

    override fun init() {
        setToolbar(
            ToolbarConfig(
                resources.getString(R.string.activity_face_auth),
                isShowBack = true,
                isShowMenu = false
            )
        )

        initVM()

        initListener()
    }

    private fun initListener() {
        getDataBinding().btRequestTokenFaceAuth.setOnClickListener {
            requestToken()
        }

        getDataBinding().btTakePhotoFaceAuth.setOnClickListener {
            takePhoto()
        }

        getDataBinding().btRegisterFace.setOnClickListener {
            if (base64.isEmpty()) return@setOnClickListener
            val map = NetConfig.buildRegisterFaceMap(
                base64,
                ImageType.BASE64,
                "admin_llj",
                "living_temp",
                "tenp photo",
                ActionType.APPEND
            )
            viewMode.registerFace(map)

        }

    }

    private fun requestToken() = lifecycleScope.launch {
        val token = viewMode.getToken()
        if (token != false.toString()) {
            withContext(Dispatchers.Main) {
                ToastUtils.toastShort("token get suc")
            }
        } else {
            withContext(Dispatchers.Main) {
                ToastUtils.toastShort("token get err")
            }
        }
    }

    private fun initVM() {
        viewMode = initViewModel<FaceAuthViewModel>()
    }

    private fun dealwithPhoto() = lifecycleScope.launch(Dispatchers.IO) {
        try {
//            LogUtils.d(TAG, "imageUri:$imageUri")
//            LogUtils.d(TAG, "imageUri authority:${imageUri.authority}")
//            LogUtils.d(TAG, "imageUri path:${imageUri.path}")
//            return@launch

            // 将拍摄的照片显示出来
            LogUtils.d(TAG, "set photo start")

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun takePhoto() {
        val outputImage = File(externalCacheDir, "output_image.jpg")
        try {
            if (outputImage.exists()) {
                outputImage.delete()
            }
            outputImage.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        imageUri = if (Build.VERSION.SDK_INT < 24) {
            Uri.fromFile(outputImage)
        } else {
            FileProvider.getUriForFile(
                this,
                "com.llj.living.fileprovider",//定义唯一标识，关联后面的内容提供器
                outputImage
            )
        }

        mLauncher.launch(
            Intent("android.media.action.IMAGE_CAPTURE").putExtra(
                MediaStore.EXTRA_OUTPUT,
                imageUri
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        //释放图片资源
        bitmap?.let {
            if (it.isRecycled) {
                bitmap?.recycle()
                bitmap = null
            }
        }
    }

    fun getBitmapFromScale(): Bitmap? {
        bitmap?.let {
//            val byteCount = it.byteCount / 1024
//            LogUtils.d("FaceAuthenticActivity", "byteCount $byteCount")
            //压缩倍数
            var scaleWidth = 1f
            var scaleHeight = 1f
            val mWidth = it.width
            val mHeight = it.height
            if (mWidth <= mHeight) {
                scaleWidth = (mWidth / 2).toFloat()
                scaleHeight = (mHeight / 2).toFloat()
            } else {
                scaleWidth = (mWidth / 2).toFloat()
                scaleHeight = (mHeight / 2).toFloat()
            }
            val matrix = Matrix()
            // 按照固定宽高进行缩放
            matrix.postScale(0.3f, 0.3f)
            return Bitmap.createBitmap(it, 0, 0, mWidth, mHeight, matrix, true)
        }
        bitmap?.recycle()
        return null
    }
}