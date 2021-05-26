package com.llj.living.ui.activity

import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.llj.living.R
import com.llj.living.custom.ext.*
import com.llj.living.data.bean.SysTimeBean
import com.llj.living.data.const.Const
import com.llj.living.databinding.ActivitySplashBinding
import com.llj.living.net.repository.SystemRepository
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.*

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getLayoutId() = R.layout.activity_splash

    override fun isScreenFull() = true

    override fun showToolbar() = false

    private val timeLiveData = MutableLiveData<Int>(5)
    private var timeIsOk = false

    override fun init() {
        super.init()
        setClickFunction()
        setTimer()
        getSysTime()
        updateSpTime()
    }

    private fun updateSpTime() {
        val sp = getSP(Const.SPMySqlNet)
        val savedDate = sp.getInt(Const.SPMySqlTodayReminderUpdateTime, 0)
        val nowDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        if (nowDate != savedDate) {
            sp.save {
                putBoolean(Const.SPMySqlTodayReminderUpdate, false)
                putInt(Const.SPMySqlTodayReminderUpdateTime, nowDate)
            }
        }
    }

    private fun setClickFunction() {
        val tvTime = findViewById<TextView>(R.id.tvTime)
        timeLiveData.baseObserver(this) {
            tvTime.text = if (timeIsOk) {
                tvTime.visibility = View.VISIBLE
                " 关闭 "
            } else it.toString()
        }
        tvTime.setOnClickListener {
            startCommonFinishedActivity<LoginActivity>()
        }
    }

    private fun getSysTime() = tryExceptionLaunch(errTips = "服务器时间") {
        val bean = SystemRepository.getSysTimeRequest()
        LogUtils.d(TAG, bean.toString())
        val versionBean = baseBeanConverter<SysTimeBean>(bean)
        val cTime = System.currentTimeMillis() / 1000
        if (cTime - versionBean.time > 60 * 60 * 24) {
            toastShort("服务器时间校验失败")
            delay(1000)
            finish()
        } else {
            LogUtils.d("SplashActivityTEST",bean.toString())
            timeIsOk = true
        }
    }

    private fun setTimer() = commonLaunch {
        while (true) {
            delay(1000)
            val newTime = timeLiveData.value!! - 1
            timeLiveData.postValue(newTime)
            if (newTime == 0) {
                break
            }
        }
        if (timeIsOk) {
            startCommonFinishedActivity<LoginActivity>()
        } else {
            withContext(Dispatchers.Main) {
                toastShort("时间校验失败")
            }
            delay(1000)
            finish()
        }
    }


}


