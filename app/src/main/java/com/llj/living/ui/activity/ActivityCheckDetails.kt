package com.llj.living.ui.activity

import android.os.Build
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.custom.ext.*
import com.llj.living.data.bean.*
import com.llj.living.data.const.Const
import com.llj.living.data.enums.TakePhotoEnum
import com.llj.living.databinding.ActivityCheckDetailBinding
import com.llj.living.logic.vm.CheckDetailsVM
import com.llj.living.utils.LogUtils
import com.llj.living.utils.PhotoUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivityCheckDetails : BaseTPActivity<ActivityCheckDetailBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_check_detail

    override fun setToolbar() = ToolbarConfig("核查详情", isShowBack = true, isShowMenu = false)

    private val checkDetailVM by viewModels<CheckDetailsVM>()

    private var peopleId: Int = -1
    private var checkId: Int = -1

    private var checkByIdBean: InfoByEntIdBean? = null

    override fun init() {

        peopleId = intent.getIntExtra(INTENT_ID_CHECK_FLAG, -1)
        checkId = getSP(Const.SPCheck).getInt(Const.SPCheckCheckId, -1)

        checkByIdBean =
            intent.getParcelableExtra<InfoByEntIdBean>(INTENT_BEAN_CHECK_FLAG)

        if (peopleId == -1 || checkId == -1 || checkByIdBean == null) {
            ToastUtils.toastShort("数据解析错误 请返回重试")
            finish()
            return
        }

        getDataBinding().apply {

            lifecycleOwner = this@ActivityCheckDetails

            checkVm = checkDetailVM

            peopleBean = checkByIdBean

            takePhotoFragment.setOnClickListener {
                lifecycleScope.launch {
                    launchOldManPhoto.launch(getPhotoIntent(TakePhotoEnum.CheckFace))
                }
            }

            btCompleted.setOnClickListener {
                lifecycleScope.launch {
                    launchOldManPhoto.launch(getPhotoIntent(TakePhotoEnum.CheckFace))
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private suspend fun getFacePhoto() {
        buildActivityCoroutineDialog(layoutInflater, null) { bd, ld ->
            try {
                bd.close.text = "取消"
                bd.tvTipsStr.text = "照片真实性检测中"
                val verifyResult = checkDetailVM.verifyFaceRealness()
                LogUtils.d("${TAG}_TT", verifyResult.toString())

                if (verifyResult.result.isJsonNull) throw Exception("非正常拍摄人脸")
                val faceRealnessBean = baseBaiduBeanConverter<FaceRealBean>(verifyResult)
                if (faceRealnessBean.face_liveness <= 0.8) {
                    throw Exception("未检测到真实人脸")
                }

                bd.tvTipsStr.text = "人脸搜索中"
                val baiduResult = checkDetailVM.searchBaiduInfo()
                LogUtils.d("${TAG}_TT", baiduResult.toString())
                if (baiduResult.error_code.isBaiduCodeSuc() && baiduResult.error_msg.isBaiduMsgSuc()) {
                    if (baiduResult.result.isJsonNull) throw Exception("无相似人脸")
                    val searchFaceBean = baseBaiduBeanConverter<SearchFaceBean>(baiduResult)
                    val isSameUser =
                        searchFaceBean.user_list.find { it.score >= 80 && it.user_id.split("_")[1] == checkByIdBean!!.id_number }
                    if (isSameUser != null) {

                        bd.tvTipsStr.text = "服务器验证中"
                        getDataBinding().tvCheckTime.text =
                            System.currentTimeMillis().toSimpleTime()
                        val result = checkDetailVM.verifyIdNumber(
                            checkId,
                            peopleId,
                            faceRealnessBean.face_liveness.toInt(),
                            isSameUser.score.toInt(),
                            proportion.value
                        )
                        if (result.code.isCodeSuc() && result.msg.isMsgSuc()) {
                            ToastUtils.toastShort("匹配成功")
                            checkDetailVM.setPhotoInfo(true)
                            ld?.cancel()
                            delay(1000)
                            finish()
                        } else throw Exception("服务器验证失败")
                    } else throw Exception("人脸匹配失败")
                } else throw Exception("人脸库操作失败:${baiduResult.error_msg}")
            } catch (e: Exception) {
                getDataBinding().btCompleted.visibility = View.VISIBLE //确认已拍照后 显示“重试”按钮
                ToastUtils.toastShort(e.message.toString())
                e.printStackTrace()
                ld?.cancel()
            }
        }
    }

    /**
     * 构建照相启动模式
     */
    private val launchOldManPhoto by lazy {
        buildLaunch {
            it?.let { bp ->
                getDataBinding().tvTips.visibility = View.INVISIBLE
                getDataBinding().ivOldManPhoto.setImageBitmap(bp)
                val base64str = PhotoUtils.bitmapToBase64(bp)
                checkDetailVM.setBase64Str(base64str)
                commonLaunch(Dispatchers.Main) {
                    getFacePhoto()
                }
            }
        }
    }

    companion object {
        const val INTENT_ID_CHECK_FLAG = "intent_id_check_detail_flag"
        const val INTENT_BEAN_CHECK_FLAG = "intent_bean_check_detail_flag"
    }
}