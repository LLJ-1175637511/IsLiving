package com.llj.living.ui.activity

import android.os.Build
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.custom.exception.IdcardBErrException
import com.llj.living.custom.exception.IdcardBReversedException
import com.llj.living.custom.exception.IdcardFErrException
import com.llj.living.custom.exception.IdcardFReversedException
import com.llj.living.custom.ext.*
import com.llj.living.data.bean.InfoByEntIdBean
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.data.const.Const
import com.llj.living.data.enums.ImageStatusEnum
import com.llj.living.data.enums.TakePhotoEnum
import com.llj.living.databinding.ActivitySupplementInfoBinding
import com.llj.living.logic.vm.SuppleDetailsVM
import com.llj.living.utils.LogUtils
import com.llj.living.utils.PhotoUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivitySuppleDetails : BaseTPActivity<ActivitySupplementInfoBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_supplement_info

    override fun setToolbar() = (ToolbarConfig("补录信息", isShowBack = true, isShowMenu = false))

    private val isAllCompleted = mutableSetOf<Int>()

    private val viewModel by viewModels<SuppleDetailsVM>()

    private var peopleId: Int = -1
    private var reputId: Int = -1

    @RequiresApi(Build.VERSION_CODES.R)
    override fun init() {

        peopleId = intent.getIntExtra(INTENT_ID_SUPPLE_FLAG, -1)
        reputId = getSP(Const.SPAddons).getInt(Const.SPAddonsReputId, -1)

        val addonsByIdBean =
            intent.getParcelableExtra<InfoByEntIdBean>(INTENT_BEAN_SUPPLE_FLAG)

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

            btFinishSuppleInfo.setOnClickListener {
                commonLaunch(Dispatchers.Main) {
                    uploadPicture(addonsByIdBean.id_number)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private suspend fun uploadPicture(idNumber: String) {
        buildActivityCoroutineDialog(layoutInflater, null) { bd, ld ->
            try {
                bd.close.text = "取消"
                bd.tvTipsStr.text = "身份证核验中"
                val idARequest = viewModel.checkIDCardAFront()
                val idBRequest = viewModel.checkIDCardBack()
                val idAResult = idARequest.await()
                val idBResult = idBRequest.await()
                LogUtils.d(
                    "${TAG}_TT",
                    "idAResult:${idAResult.image_status} idBResult:${idBResult.image_status}"
                )
                LogUtils.d("${TAG}_TT", "idAResult:${idAResult} idBResult:${idBResult.toString()}")

                when (idAResult.image_status) {
                    ImageStatusEnum.non_idcard.name, ImageStatusEnum.other_type_card.name, ImageStatusEnum.unknown.name -> {
                        throw IdcardFErrException()
                    }
                    ImageStatusEnum.reversed_side.name->{
                        throw IdcardFReversedException()
                    }
                    else -> {
                    }
                }

                when (idBResult.image_status) {
                    ImageStatusEnum.non_idcard.name, ImageStatusEnum.other_type_card.name, ImageStatusEnum.unknown.name -> {
                        throw IdcardBErrException()
                    }
                    ImageStatusEnum.reversed_side.name->{
                        throw IdcardBReversedException()
                    }
                    else -> {
                    }
                }

                bd.tvTipsStr.text = "人脸信息注册中"
                val baiduResult = viewModel.uploadBaiduInfo(idNumber, peopleId)
                LogUtils.d("${TAG}_TT", baiduResult.toString())

                if (baiduResult.error_code.isBaiduCodeSuc() && baiduResult.error_msg.isBaiduMsgSuc()) {
                    bd.tvTipsStr.text = "服务器图片上传中"
                } else if (baiduResult.error_code==222202&& baiduResult.error_msg=="pic not has face"){
                    throw Exception("图片未检测到人脸")
                }else{
                    throw Exception("人脸库注册失败")
                }
                val myResult = viewModel.uploadPictureInfo(
                    reputId,
                    peopleId,
                    proportion.value
                )
                if (myResult.code.isCodeSuc() && myResult.msg.isMsgSuc()) {
                    bd.tvTipsStr.text = "上传成功"
                    delay(1000)
                    ld?.cancel()
                    finish()
                } else {
                    LogUtils.d("${TAG}_DEBUG", "result:${myResult}")
                    throw Exception("服务器图片上传失败")
                }
            } catch (e: Exception) {
                ToastUtils.toastShort("失败：${e.message}")
                e.printStackTrace()
                ld?.cancel()
            }
        }
    }

    private fun takePhoto(personFace: TakePhotoEnum) {
        lifecycleScope.launch {
            when (personFace) {
                TakePhotoEnum.PersonFace -> faceLaunch.launch(getPhotoIntent(personFace))
                TakePhotoEnum.IdcardFront -> frontLaunch.launch(getPhotoIntent(personFace))
                TakePhotoEnum.IdcardBehind -> behindLaunch.launch(getPhotoIntent(personFace))
                else -> {
                }
            }
        }
    }

    private val faceLaunch by lazy {
        buildLaunch { bitmap ->
            bitmap?.let {
                getDataBinding().apply {
                    ivFaceSuppleInfo.setImageBitmap(it)
                    val base64str = PhotoUtils.bitmapToBase64(it)
                    viewModel.setBase64Str(base64str)
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
                    val base64str = PhotoUtils.bitmapToBase64(it)
                    viewModel.setIdCardABase64Str(base64str)
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
                    val base64str = PhotoUtils.bitmapToBase64(it)
                    viewModel.setIdCardBBase64Str(base64str)
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

    companion object {
        const val INTENT_ID_SUPPLE_FLAG = "intent_id_supple_detail_flag"
        const val INTENT_BEAN_SUPPLE_FLAG = "intent_bean_supple_detail_flag"
    }
}