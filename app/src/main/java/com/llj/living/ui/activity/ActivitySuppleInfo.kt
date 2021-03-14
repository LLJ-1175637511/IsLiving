package com.llj.living.ui.activity

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.data.enums.TakePhotoEnum
import com.llj.living.databinding.ActivitySupplementInfoBinding
import kotlinx.coroutines.launch

class ActivitySuppleInfo:BaseTPActivity<ActivitySupplementInfoBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_supplement_info

    private val isAllCompleted = mutableSetOf<Int>()

    override fun init() {
        setToolbar(ToolbarConfig("补录信息",isShowBack = true,isShowMenu = false))
        getDataBinding().apply {
            tvTakePhotoFace.setOnClickListener {
                takePhoto(TakePhotoEnum.PersonFace)
            }
            tvTakePhotoIdA.setOnClickListener {
                takePhoto(TakePhotoEnum.IdcardFront)
            }
            tvTakePhotoIdB.setOnClickListener {
                takePhoto(TakePhotoEnum.IdcardBehind)
            }
            btFinishSuppleInfo.setOnClickListener {
                finish()
            }
        }
    }

    private fun takePhoto(personFace: TakePhotoEnum) {
        lifecycleScope.launch {
            when(personFace){
                TakePhotoEnum.PersonFace->faceLaunch.launch(getPhotoIntent(personFace))
                TakePhotoEnum.IdcardFront->frontLaunch.launch(getPhotoIntent(personFace))
                TakePhotoEnum.IdcardBehind->behindLaunch.launch(getPhotoIntent(personFace))
            }
        }
    }

    private val faceLaunch by lazy {
        buildLaunch {
            if (it==null) return@buildLaunch
            getDataBinding().apply {
                ivFaceSuppleInfo.setImageBitmap(it)
                tvTakePhotoFace.visibility = View.INVISIBLE
            }
            checkIsCompleted(1)
        }
    }

    private val frontLaunch by lazy {
        buildLaunch {
            if (it==null) return@buildLaunch
            getDataBinding().apply {
                ivIdCardASuppleInfo.setImageBitmap(it)
                tvTakePhotoIdA.visibility = View.INVISIBLE
            }
            checkIsCompleted(2)
        }
    }

    private val behindLaunch by lazy {
        buildLaunch {
            if (it==null) return@buildLaunch
            getDataBinding().apply {
                ivIdCardBSuppleInfo.setImageBitmap(it)
                tvTakePhotoIdB.visibility = View.INVISIBLE
            }
            checkIsCompleted(3)
        }
    }

    private fun checkIsCompleted(tag:Int){
        isAllCompleted.add(tag)
        if (isAllCompleted.size >= 3){
            getDataBinding().btFinishSuppleInfo.visibility = View.VISIBLE
        }
    }

}