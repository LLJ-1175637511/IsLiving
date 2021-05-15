package com.llj.living.ui.activity

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.custom.ext.baseObserver
import com.llj.living.custom.ext.getSP
import com.llj.living.custom.ext.toSimpleTime
import com.llj.living.data.bean.InfoByEntIdBean
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.data.const.Const
import com.llj.living.data.enums.TakePhotoEnum
import com.llj.living.databinding.ActivityCheckDetailBinding
import com.llj.living.logic.vm.CheckDetailsVM
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivityCheckDetail : BaseTPActivity<ActivityCheckDetailBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_check_detail

    private val checkDetailVM by viewModels<CheckDetailsVM>()

    private var peopleId: Int = -1
    private var reputId: Int = -1

    override fun init() {
        setToolbar(
            ToolbarConfig(
                getString(R.string.check_detail),
                isShowBack = true,
                isShowMenu = false
            )
        )

        peopleId = intent.getIntExtra(INTENT_ID_CHECK_FLAG, -1)
        reputId = getSP(Const.SPCheck).getInt(Const.SPCheckReputId, -1)

        val checkByIdBean =
            intent.getParcelableExtra<InfoByEntIdBean>(ActivitySuppleDetails.INTENT_BEAN_SUPPLE_FLAG)

        if (peopleId == -1 || reputId == -1 || checkByIdBean == null) {
            ToastUtils.toastShort("数据解析错误 请返回重试")
            finish()
            return
        }

        checkDetailVM.photoInfo.baseObserver(this) {

        }

        getDataBinding().apply {

            peopleBean = checkByIdBean

            takePhotoFragment.setOnClickListener {
                lifecycleScope.launch {
                    launchOldManPhoto.launch(getPhotoIntent(TakePhotoEnum.PersonFace))
                }
            }

            btCompleted.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {

                    delay(500)
                    finish()
                }
            }
        }
    }

    private fun getFacePhoto() {
        lifecycleScope.launch {
            getDataBinding().apply {
                btCompleted.visibility = View.VISIBLE
                tvTipsTakePhoto.visibility = View.INVISIBLE
                tvTips.visibility = View.INVISIBLE
                tvCheckTime.text = System.currentTimeMillis().toSimpleTime()
//                toastShort("识别成功")
            }
        }
    }

    /**
     * 构建照相启动模式
     */
    private val launchOldManPhoto by lazy {
        buildLaunch {
            it?.let { bp ->
                getDataBinding().ivOldManPhoto.setImageBitmap(bp)
                /*val base64 = PhotoUtils.bitmapToBase64(bp).toString()*/
                getFacePhoto()
            }
        }
    }

    companion object {
        const val INTENT_ID_CHECK_FLAG = "intent_id_check_detail_flag"
        const val INTENT_BEAN_CHECK_FLAG = "intent_bean_check_detail_flag"
    }
}