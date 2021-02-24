package com.llj.living.ui.activity

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.llj.living.R
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_main

    private val TAG = this.javaClass.simpleName

    override fun init() {
        buildToolbar()
        initListener()

    }

    private fun initListener() {
        getDataBinding().btTakePhoto.setOnClickListener {
            startCommonActivity<TakePhotoActivity>()
        }
        getDataBinding().btStartToFaceAuth.setOnClickListener {
            startCommonActivity<FaceAuthenticActivity>()
        }
    }

    private fun buildToolbar() {
        setToolbar(ToolbarConfig(title = TAG, isShowBack = false, isShowMenu = true))
        val ivToolbar = getToolbar()?.findViewById<ImageView>(R.id.toolbar_base_img)
        ivToolbar?.let {
            Glide.with(this)
                .load(R.mipmap.logo)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(ivToolbar)
        }
    }
}