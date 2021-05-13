package com.llj.living.ui.activity

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.data.bean.AddonsByEntIdBean
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.data.enums.IsShowedType
import com.llj.living.data.enums.TakePhotoEnum
import com.llj.living.databinding.ActivitySupplementInfoBinding
import com.llj.living.logic.vm.SuppleInfoVM
import com.llj.living.ui.adapter.*
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.launch

class ActivitySuppleInfo : BaseTPActivity<ActivitySupplementInfoBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_supplement_info

    private val isAllCompleted = mutableSetOf<Int>()

    private val viewModel by viewModels<SuppleInfoVM>()
//    private val dbViewModel by viewModels<DatabaseVM>()
//    private var oldManInfoWait: OldManInfoWait? = null

    private var id:Int = -1

    override fun init() {
        setToolbar(ToolbarConfig("补录信息", isShowBack = true, isShowMenu = false))
        id = intent.getIntExtra(SupplementWaitTestAdapter.SUPPLE_ID_WAIT_FLAG, -1)

        val addonsByIdBean =
            intent.getParcelableExtra<AddonsByEntIdBean>(SupplementWaitTestAdapter.SUPPLE_BEAN_WAIT_FLAG)

        if (id == -1 || addonsByIdBean == null) {
            ToastUtils.toastShort("数据解析错误 请返回重试")
            finish()
            return
        }

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
                /*oldManInfoWait?.let {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val suppleDoingId = SuppleDoingAdapter.id
                        dbViewModel.finishedOneInfo(it, suppleDoingId)
                        delay(500)
                        finish()
                    }
                }*/
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
            bitmap.let {
                getDataBinding().apply {
                    ivFaceSuppleInfo.setImageBitmap(it)
                    viewModel.setShowedUi(IsShowedType.Face)
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
                    viewModel.setShowedUi(IsShowedType.IdCardA)
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
                    viewModel.setShowedUi(IsShowedType.IdCardB)
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