package com.llj.living.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.custom.ext.toastShort
import com.llj.living.data.bean.MatchFaceData
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.data.enums.ActionType
import com.llj.living.data.enums.ImageType
import com.llj.living.data.enums.ModifyFaceType
import com.llj.living.databinding.ActivityFaceAuthBinding
import com.llj.living.logic.vm.FaceAuthViewModel
import com.llj.living.net.config.BadiduNetConfig
import com.llj.living.utils.LogUtils
import com.llj.living.utils.PhotoUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class FaceAuthenticActivity : BaseActivity<ActivityFaceAuthBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_face_auth

    private val viewMode by viewModels<FaceAuthViewModel>()
    private var bitmap: Bitmap? = null
    private val ops = BitmapFactory.Options()

    init {
        ops.apply {
            inSampleSize = 4 //设置缩放比例（必须为2的倍数）
            inPremultiplied = true //设置可回收
            inPreferredConfig = Bitmap.Config.ARGB_8888 //设置编码方式 普通一像素2字节 默认为当前4字节
        }
    }

    private val mLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            try {
                bitmap =
                    BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri), null, ops)
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
                toastShort("图片过大 内存溢出")
            }
            if (bitmap == null) return@registerForActivityResult
            getDataBinding().ivFaceImgFaceAuth.setImageBitmap(bitmap)
            base64 = PhotoUtils.bitmapToBase64(bitmap).toString()
            LogUtils.d(TAG, "base64 size:${base64.length}")
            viewMode.getContentLiveData().postValue("base64 获取成功")
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

        getDataBinding().btUpdateFace.setOnClickListener {
            setBaiduFace(ModifyFaceType.Update)
        }

        getDataBinding().btRegisterFace.setOnClickListener {
            setBaiduFace(ModifyFaceType.Register)
        }

        getDataBinding().btDeleteFace.setOnClickListener {
            deleteBaiduFace()
        }

        getDataBinding().btFaceMatchAuth.setOnClickListener {
            matchFace()
        }

        getDataBinding().btSearch.setOnClickListener {
            searchFace()
        }

        getDataBinding().btSearchZN.setOnClickListener {
            searchFaceInZN()
        }
    }

    /**
     * 注册或更新人脸
     */
    private fun setBaiduFace(type: ModifyFaceType) {
        if (base64.isEmpty()) return
        val map = BadiduNetConfig.buildRegisterOrUpdateFaceMap(
            base64,
            ImageType.BASE64,
            "admin_llj_2",
            "living_temp",
            "temp photo",
            ActionType.APPEND //暂时不需要此参数
        )
        viewMode.modifyFace(map, type)
    }

    private fun searchFace() {
        if (base64.isEmpty()) return
        val map = BadiduNetConfig.buildSearchFaceMap(
            base64,
            ImageType.BASE64,
            "admin_llj_2"
        )
        viewMode.searchFace(map)
    }

    private fun searchFaceInZN() {
        if (base64.isEmpty()) return
        val map = BadiduNetConfig.buildSearchFaceInZnMap(
            base64,
            ImageType.BASE64,
            "刘林杰",
            "412726199802197177"
        )
        viewMode.searchFaceInZn(map)
    }

    private fun matchFace() {
        val baseId = viewMode.getBaseFaceIdLiveData().value
        if (baseId.isNullOrEmpty() || base64.isEmpty()) return
        val list = mutableListOf<MatchFaceData>()
        val baseBean = MatchFaceData(baseId, ImageType.FACE_TOKEN)
        list.add(baseBean)
        val needAuthBean = MatchFaceData(base64, ImageType.BASE64)
        list.add(needAuthBean)
        viewMode.matchFace(list)
    }

    /**
     * 删除对应face_token的人脸
     */
    private fun deleteBaiduFace() {
        val id = viewMode.getPhotoIdLiveData().value
        if (id.isNullOrEmpty()) return
        val map = BadiduNetConfig.buildDeleteFaceMap(
            "admin_llj_2",
            "living_temp",
            id
        )
        viewMode.deleteFace(map)
    }

    private fun requestToken() = lifecycleScope.launch {
        val token = viewMode.getToken()
        if (token != false.toString()) {
            withContext(Dispatchers.Main) {
                toastShort("token get suc")
            }
        } else {
            withContext(Dispatchers.Main) {
                toastShort("token get err")
            }
        }
    }

    private fun initVM() {
        getDataBinding().vmFaceAuth = viewMode //使用dataBinding和textView联动 切记初始化xml中的vm
        getDataBinding().lifecycleOwner = this
//        viewMode.getBaseFaceIdLiveData().baseObserver(this){
//            = "043bb81e9edb4457475d354a312a37f7"
//        }
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
}