package com.llj.living.ui.activity

import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.llj.living.R
import com.llj.living.application.MyApplication
import com.llj.living.custom.ext.*
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.data.bean.VersionBean
import com.llj.living.data.const.Const
import com.llj.living.data.enums.VersionUpdateEnum
import com.llj.living.databinding.ActivityLoginBinding
import com.llj.living.databinding.DialogUpdateCommonBinding
import com.llj.living.databinding.DialogUpdateForceBinding
import com.llj.living.logic.vm.LoginViewModel
import com.llj.living.utils.LogUtils

class LoginActivity : BaseBLActivity<ActivityLoginBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_login

    private val viewModel by viewModels<LoginViewModel>()

    private val TAG = "${this.javaClass.simpleName}TEST"

    override fun init() {
        setToolbar(ToolbarConfig(title = "用户登录", isShowBack = false, isShowMenu = false))
        initVM()
        initLD()
    }

    private fun initLD() {
        viewModel.loginResultLiveData.baseObserver(this) { isSuc ->
            if (isSuc) { //登录成功
                startCommonFinishedActivity<MainActivity>()
            }
        }
        viewModel.toastMsgLiveData.baseObserver(this) {
            if (it.isNotEmpty()) toastShort(it)
        }
        viewModel.versionLiveData.baseObserver(this) {
            buildAlertDialog(VersionUpdateEnum.REMIND, it.second)
        }
        locationLiveData.baseObserver(this) {
            viewModel.setLocation(it)
        }
        viewModel.isRemindUpdateLiveData.baseObserver(this) {
            if (it){
                val now = System.currentTimeMillis()/1000
                getSP(Const.SPMySqlNet).save {
                    putBoolean(Const.SPMySqlTodayReminderUpdate,it)
                    putLong(Const.SPMySqlTodayReminderUpdateTime,now)
                }
            }
        }
    }

    private fun buildAlertDialog(
        ue: VersionUpdateEnum,
        versionData: VersionBean
    ) {
        AlertDialog.Builder(this).apply {
            if (ue == VersionUpdateEnum.FORCE) {  //强制更新界面
                LogUtils.d(TAG, "强制更新")
                val viewBinding = buildView<DialogUpdateForceBinding>(R.layout.dialog_update_force)
                viewBinding.apply {
                    lifecycleOwner = this@LoginActivity
                    vm = viewModel
                    val versionId = "${getString(R.string.new_version)}${versionData.newversion}"
                    tvVersionId.text = versionId
                    tvStartLoad.setOnClickListener {
                        vm.loadNewApk()
                    }
                    setCancelable(false)
                }
            } else { //提示更新界面
                LogUtils.d(TAG, "提醒更新")
                val viewBinding =
                    buildView<DialogUpdateCommonBinding>(R.layout.dialog_update_common)
                viewBinding.apply {
                    lifecycleOwner = this@LoginActivity
                    vm = viewModel
                    val versionId = "${getString(R.string.new_version)}${versionData.newversion}"
                    tvVersionId.text = versionId
                    setCancelable(false)
                    tvDownloadNew.setOnClickListener {
                        vm.loadNewApk()
                    }
                    tvContinueLogin.setOnClickListener {
                        vm.login()
                    }
                }
            }
        }
    }

    private fun <DB : ViewDataBinding> buildView(@LayoutRes resId: Int) =
        DataBindingUtil.inflate<DB>(
            layoutInflater,
            resId,
            getDataBinding().root as ViewGroup,
            false
        )

    private fun initVM() {
        getDataBinding().loginVm = viewModel
        val versionStr =
            "${resources.getString(R.string.user_version)}${MyApplication.CURRENT_VERSION}"
        getDataBinding().tvVersionLogin.text = versionStr
        viewModel.loadUserData()
    }


}