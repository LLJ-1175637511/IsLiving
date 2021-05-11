package com.llj.living.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
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
import com.llj.living.databinding.DialogLoadApkBinding
import com.llj.living.databinding.DialogUpdateCommonBinding
import com.llj.living.databinding.DialogUpdateForceBinding
import com.llj.living.logic.vm.LoginViewModel
import com.llj.living.utils.LogUtils
import java.io.File
import java.util.*

class LoginActivity : BaseBLActivity<ActivityLoginBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_login

    private val viewModel by viewModels<LoginViewModel>()

    private var dialog: AlertDialog? = null //提示更新、强制更新 dialog

    private var versionId = ""

    private val loadDialog by lazy {
        AlertDialog.Builder(this).apply {
            setCancelable(false)
            val viewBinding = buildView<DialogLoadApkBinding>(R.layout.dialog_load_apk)
            viewBinding.vm = viewModel
            val tips = "$versionId 下载中"
            viewBinding.tvTips.text = tips
            setView(viewBinding.root)
        }.create()
    }

    override fun init() {
        setToolbar(ToolbarConfig(title = "用户登录", isShowBack = false, isShowMenu = false))
        initVM()
        initLD()
    }

    private fun initLD() {
        viewModel.loginLiveData.baseObserver(this) {
            //登录成功
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(ENT_BEAN_FLAG, it)
            }
            startCommonFinishedActivity<MainActivity>(intent)
        }
        viewModel.toastMsgLiveData.baseObserver(this) {
            if (it.isNotEmpty()) toastShort(it)
        }

        viewModel.versionLiveData.baseObserver(this) {
            buildAlertDialog(it.first, it.second)
        }

        viewModel.cancelLoadApkDialogLD.baseObserver(this) {
            loadDialog.let { ad ->
                if (ad.isShowing && it) {
                    ad.cancel()
                }
            }
        }
        viewModel.installLiveData.baseObserver(this) {
            installAPK(it)
        }
    }

    private fun buildAlertDialog(
        ue: VersionUpdateEnum,
        versionData: VersionBean
    ) {
        dialog = AlertDialog.Builder(this).apply {
            setCancelable(false) //设置触碰外界不可销毁当前dialog
            versionId = "${getString(R.string.new_version)}${versionData.newversion}"
            when (ue) {
                VersionUpdateEnum.FORCE -> {
                    LogUtils.d(TAG, "强制更新")
                    val viewBinding =
                        buildView<DialogUpdateForceBinding>(R.layout.dialog_update_force).apply {
                            tvVersionId.text = versionId
                            ivCancel.setOnClickListener {
                                dialog?.cancel()
                            }
                            tvStartLoad.setOnClickListener {
                                dialog?.cancel()
                                buildLoadApkDialog()
                                viewModel.loadNewApk()
                            }
                        }
                    setView(viewBinding.root)
                }
                VersionUpdateEnum.REMIND -> {
                    LogUtils.d(TAG, "提醒更新")
                    val viewBinding =
                        buildView<DialogUpdateCommonBinding>(R.layout.dialog_update_common).apply {
                            tvVersionId.text = versionId
                            cbIsRemind.setOnClickListener {
                                getSP(Const.SPMySqlNet).save {
                                    putBoolean(
                                        Const.SPMySqlTodayReminderUpdate,
                                        cbIsRemind.isChecked
                                    )
                                    putInt(
                                        Const.SPMySqlTodayReminderUpdateTime,
                                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                                    )
                                }
                            }
                            ivCancel.setOnClickListener {
                                dialog?.cancel()
                            }
                            tvLoadNew.setOnClickListener {
                                dialog?.cancel()
                                buildLoadApkDialog()
                                viewModel.loadNewApk()
                            }
                            tvLogin.setOnClickListener {
                                dialog?.cancel()
                                viewModel.login()
                            }
                        }
                    setView(viewBinding.root)
                }
            }
        }.create()
        dialog?.show()
    }

    private fun buildLoadApkDialog() {
        loadDialog.show()
    }

    private fun <DB : ViewDataBinding> buildView(@LayoutRes resId: Int) =
        DataBindingUtil.inflate<DB>(
            layoutInflater,
            resId,
            getDataBinding().root as ViewGroup,
            false
        ).apply {
            lifecycleOwner = this@LoginActivity
        }

    private fun initVM() {
        getDataBinding().loginVm = viewModel
        val versionStr =
            "${resources.getString(R.string.user_version)}${MyApplication.CURRENT_VERSION}"
        getDataBinding().tvVersionLogin.text = versionStr
        viewModel.loadUserData()
    }

    private fun installAPK(apkFile: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        val type = "application/vnd.android.package-archive"
        if (Build.VERSION.SDK_INT < 23) {
            intent.apply {
                setDataAndType(Uri.fromFile(apkFile), type)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        } else {
            val uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider",apkFile)
            intent.apply {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                LogUtils.d(TAG,"$apkFile")
                setDataAndType(uri, type)
            }
            try {
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                toastShort("没有找到打开此类文件的程序")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelLoadApk()
    }

    companion object {
        const val ENT_BEAN_FLAG = "ent_bean_flag"
    }
}