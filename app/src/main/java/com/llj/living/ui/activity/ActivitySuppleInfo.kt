package com.llj.living.ui.activity

import android.os.Build
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.custom.ext.*
import com.llj.living.data.bean.AddonsByEntIdBean
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.data.const.Const
import com.llj.living.data.enums.TakePhotoEnum
import com.llj.living.databinding.ActivitySupplementInfoBinding
import com.llj.living.logic.vm.SuppleInfoVM
import com.llj.living.net.NetCode
import com.llj.living.ui.adapter.*
import com.llj.living.utils.LogUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivitySuppleInfo : BaseTPActivity<ActivitySupplementInfoBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_supplement_info

    private val isAllCompleted = mutableSetOf<Int>()

    private val viewModel by viewModels<SuppleInfoVM>()

    private var peopleId: Int = -1
    private var reputId: Int = -1

    @RequiresApi(Build.VERSION_CODES.R)
    override fun init() {
        setToolbar(ToolbarConfig("补录信息", isShowBack = true, isShowMenu = false))

        peopleId = intent.getIntExtra(SupplementWaitTestAdapter.SUPPLE_ID_WAIT_FLAG, -1)
        reputId = getSP(Const.SPAddons).getInt(Const.SPAddonsReputId, -1)

        val addonsByIdBean =
            intent.getParcelableExtra<AddonsByEntIdBean>(SupplementWaitTestAdapter.SUPPLE_BEAN_WAIT_FLAG)

        if (peopleId == -1 || reputId == -1 || addonsByIdBean == null) {
            ToastUtils.toastShort("数据解析错误 请返回重试")
            finish()
            return
        }

        LogUtils.d(TAG, "peopleId:${peopleId} reputId:${reputId}")

        getDataBinding().oldmanBean = addonsByIdBean

        getDataBinding().apply {
            asiVM = viewModel
            tvTakePhotoFace.setOnClickListener {
                takePhoto(TakePhotoEnum.PersonFace)
            }
            reFace.setOnClickListener {
                takePhoto(TakePhotoEnum.PersonFace)
            }

            tvTakePhotoIdA.setOnClickListener {
                takePhoto(TakePhotoEnum.IdcardFront)
            }

            reIdCardA.setOnClickListener {
                takePhoto(TakePhotoEnum.IdcardFront)
            }

            tvTakePhotoIdB.setOnClickListener {
                takePhoto(TakePhotoEnum.IdcardBehind)
            }
            reIdCardB.setOnClickListener {
                takePhoto(TakePhotoEnum.IdcardBehind)
            }

            btFinishSuppleInfo.setOnClickListener { _ ->
                tryExceptionLaunch (Dispatchers.Main) {
                    buildActivityCoroutineDialog(layoutInflater, null) { bd, ld ->
                        bd.tvSipsStr.text = "图片上传中"
                        bd.close.visibility = View.GONE
                        val result = viewModel.uploadPictureInfo(reputId, peopleId,proportion.value)
                        if (result.code == NetCode.SUCCESS) {
                            ld?.cancel()
                            ToastUtils.toastShort("上传成功")
                            delay(1000)
                            finish()
                        }else{
                            ToastUtils.toastShort("上传失败")
                            LogUtils.d("${TAG}_DEBUG","result:${result}")
                        }
                    }

                }
            }
        }
    }

    private fun takePhoto(personFace: TakePhotoEnum) {
        lifecycleScope.launch {
            when (personFace) {
                TakePhotoEnum.PersonFace -> faceLaunch.launch(getPhotoIntent(personFace))
                TakePhotoEnum.IdcardFront -> frontLaunch.launch(getPhotoIntent(personFace))
                TakePhotoEnum.IdcardBehind -> behindLaunch.launch(getPhotoIntent(personFace))
            }
        }
    }

    private val faceLaunch by lazy {
        buildLaunch { bitmap ->
            bitmap?.let {
                getDataBinding().apply {
                    ivFaceSuppleInfo.setImageBitmap(it)
                    viewModel.setPictureShow()
                }
                checkIsCompleted(1)
            }
        }
    }

    private val frontLaunch by lazy {
        buildLaunch { bitmap ->
            bitmap?.let {
                getDataBinding().apply {
                    ivIdCardASuppleInfo.setImageBitmap(it)
                    viewModel.setPictureAShow()
                }
                checkIsCompleted(2)
            }
        }
    }

    private val behindLaunch by lazy {
        buildLaunch { bitmap ->
            bitmap?.let {
                getDataBinding().apply {
                    ivIdCardBSuppleInfo.setImageBitmap(it)
                    viewModel.setPictureBShow()
                }
                checkIsCompleted(3)
            }
        }
    }

    private fun checkIsCompleted(tag: Int) {
        isAllCompleted.add(tag)
        if (isAllCompleted.size >= 3) {
            getDataBinding().btFinishSuppleInfo.visibility = View.VISIBLE
        }
    }

}