package com.llj.living.ui.activity

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.custom.ext.toSimpleTime
import com.llj.living.custom.ext.toastShort
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.data.database.OldManInfoWait
import com.llj.living.data.enums.TakePhotoEnum
import com.llj.living.databinding.ActivityCheckDetailBinding
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.ui.adapter.CheckDoingAdapter
import com.llj.living.ui.adapter.WaitCheckAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivityCheckDetail : BaseTPActivity<ActivityCheckDetailBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_check_detail

    //    private val TAG = this.javaClass.simpleName
    private val dbViewModel by viewModels<DatabaseVM>()
    private var bean: OldManInfoWait? = null

    override fun init() {
        setToolbar(
            ToolbarConfig(
                getString(R.string.check_detail),
                isShowBack = true,
                isShowMenu = false
            )
        )

        getDataBinding().apply {
            takePhotoFragment.setOnClickListener {
                lifecycleScope.launch {
                    launchOldManPhoto.launch(getPhotoIntent(TakePhotoEnum.PersonFace))
                }
            }
            btCompleted.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    updateData()
                    delay(500)
                    finish()
                }
            }
        }

        val id = WaitCheckAdapter.id
        dbViewModel.getOldManInfoById(id).observe(this, Observer { data ->
            data?.let {
                bean = it
            }
        })

    }

    private fun updateData() {
        bean?.let {
            dbViewModel.finishedOneHad(it, CheckDoingAdapter.id)
        }
    }

    private fun getFacePhoto() {
        lifecycleScope.launch {
            delay(500)
            bean?.let {
                getDataBinding().apply {
                    btCompleted.visibility = View.VISIBLE
                    tvTipsTakePhoto.visibility = View.INVISIBLE
                    tvTips.visibility = View.INVISIBLE
                    tvSex.text = it.sex
                    tvFullName.text = it.name
                    tvCheckIdCard.text = it.idCard
                    tvCheckTime.text = System.currentTimeMillis().toSimpleTime()
                }
                toastShort("识别成功")
            }
        }
    }

    /**
     * 构建照相启动模式
     */
    private val launchOldManPhoto by lazy {
        buildLaunch {
            getDataBinding().ivOldManPhoto.setImageBitmap(it)
            getFacePhoto()
        }
    }
}