package com.llj.living.ui.activity

import com.llj.living.R
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivityFaceAuthBinding
import com.llj.living.logic.vm.FaceAuthViewModel

class FaceAuthenticActivity:BaseActivity<ActivityFaceAuthBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_face_auth

    private lateinit var viewMode:FaceAuthViewModel

    override fun init() {
        setToolbar(ToolbarConfig(resources.getString(R.string.activity_face_auth),
            isShowBack = true,
            isShowMenu = false
        ))

        initVM()

        initListener()
    }

    private fun initListener() {
        getDataBinding().btRequestTokenFaceAuth.setOnClickListener {
            viewMode.getToken()
        }
        getDataBinding().btTakePhotoFaceAuth.setOnClickListener {

        }

    }

    private fun initVM() {
        viewMode = initViewModel<FaceAuthViewModel>()
    }

}