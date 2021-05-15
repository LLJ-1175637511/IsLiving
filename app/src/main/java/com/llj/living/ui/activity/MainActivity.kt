package com.llj.living.ui.activity

import androidx.activity.viewModels
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.llj.living.R
import com.llj.living.custom.ext.*
import com.llj.living.data.bean.LoginBean
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.data.database.OldManInfoWait
import com.llj.living.data.database.SuppleDoing
import com.llj.living.data.database.SuppleFinished
import com.llj.living.databinding.ActivityNavMainBinding
import com.llj.living.logic.vm.DatabaseTestVM
import kotlinx.coroutines.delay

class MainActivity : BaseActivity<ActivityNavMainBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_nav_main

    private val dbViewModel by viewModels<DatabaseTestVM>()

    override fun init() {
        buildToolbar()
        buildBottomView()
        initListener()
        initData()
    }

    private fun initData() {
        /*dbViewModel.insertSuppleDoing(loadData())
        dbViewModel.insertSuppleFinished(loadFinishedData())
        dbViewModel.insertOldmanInfo(loadOldManData())*/
        loadLoginBean()
    }

    private fun loadLoginBean() {
        val lb = intent.getParcelableExtra<LoginBean>(LoginActivity.ENT_BEAN_FLAG)
        if (lb == null) {
            toastShort("数据获取失败")
            commonLaunch {
                delay(1000)
                startCommonFinishedActivity<LoginActivity>()
            }
            return
        }
        dbViewModel.setEntBean(lb)
        getDataBinding().viewHeader.apply {
            lifecycleOwner = this@MainActivity
            vm = dbViewModel
            tryException(errTips = "头像") {
                //加载网络圆形图片
                loadCircleView(lb.avatar,getDataBinding().viewHeader.ivLogoMain)
            }
        }
    }

    private fun loadData() = mutableListOf<SuppleDoing>().apply {
        add(
            SuppleDoing(
                title = "补录（${(10..15).random()}周${(0..3).random()}批）",
                startTime = "2021-03-${(10..28).random()}",
                endTime = (System.currentTimeMillis() + 1000 * 60 * 60 * 24).toSimpleTime(),
                waitDealWith = 11,
                hadDealWith = 0
            )
        )
        for (i in 1..5) {
            val waitCount = (5..11).random()
            add(
                SuppleDoing(
                    title = "补录（${(10..15).random()}周${(0..3).random()}批）",
                    startTime = "2021-03-${(10..28).random()}",
                    endTime = (System.currentTimeMillis() + 1000 * 60 * 60 * 24).toSimpleTime(),
                    waitDealWith = waitCount,
                    hadDealWith = (0 until waitCount).random()
                )
            )
        }
    }

    private fun loadFinishedData() = mutableListOf<SuppleFinished>().apply {
        add(
            SuppleFinished(
                title = "补录（8周3批）",
                startTime = "2021-03-${(10..15).random()}",
                endTime = (System.currentTimeMillis() + 1000 * 60 * 60 * 24 * (1..3).random()).toSimpleTime(),
                waitDealWith = 0,
                hadDealWith = 6
            )
        )
        add(
            SuppleFinished(
                title = "补录（9周2批）",
                startTime = "2021-03-${(10..15).random()}",
                endTime = (System.currentTimeMillis() + 1000 * 60 * 60 * 24).toSimpleTime(),
                waitDealWith = 0,
                hadDealWith = 10
            )
        )
        add(
            SuppleFinished(
                title = "补录（9周1批）",
                startTime = "2021-03-${(10..15).random()}",
                endTime = (System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 3).toSimpleTime(),
                waitDealWith = 0,
                hadDealWith = 8
            )
        )
    }

    private fun loadOldManData() = mutableListOf<OldManInfoWait>().apply {
        for (i in 1..name.size) {
            add(
                OldManInfoWait(
                    name = name[i - 1],
                    idCard = idCard[i - 1],
                    sex = sex[i - 1]
                )
            )
        }
    }

    private fun buildBottomView() {
        val navController = Navigation.findNavController(this, R.id.nav_main_fragment)
        val bottomBar: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        bottomBar.setupWithNavController(navController)
    }

    private fun initListener() {

    }

    private fun buildToolbar() {
        setToolbar(
            ToolbarConfig(
                title = resources.getString(R.string.app_name),
                isShowBack = false,
                isShowMenu = true
            )
        )
    }
    /*   override fun onResume() {
           super.onResume()
           val changedCount = intent.getIntExtra(ActivitySupplement.ACTIVITY_SUPPLEMENT_COUNT, -1)
           LogUtils.d("ActivitySupplement", "changedCount${changedCount}")
           if (changedCount != -1) {
               suppleVM.setChangedSize(changedCount)
           }
       }*/

    val name = arrayOf<String>(
        "寿百易",
        "申丹洁",
        "李惜天",
        "熊问兰",
        "蒋今瑶",
        "车怀玉",
        "衡玉寒",
        "红丰雅",
        "扶雅博",
        "步松雨",
        "冉洁",
        "俞卿腾",
        "潘贝",
        "崔达",
        "补咏仪",
        "马若",
        "向义",
        "吉宝",
        "候贞",
        "孟芳",
        "赵枫",
        "耿文"
    )
    val sex = arrayOf<String>(
        "男",
        "女",
        "女",
        "女",
        "女",
        "女",
        "男",
        "女",
        "男",
        "男",
        "女",
        "男",
        "男",
        "男",
        "女",
        "女",
        "男",
        "女",
        "女",
        "女",
        "男",
        "男"
    )
    val idCard = arrayOf<String>(
        "522324196506251622",
        "522622195407155028",
        "522129196211071028",
        "522129196811251065",
        "522129196911111018",
        "220102195403072466",
        "220102195403070120",
        "11010119600307600X",
        "110101195903075226",
        "130102196003079032",
        "372324196702013754",
        "422802195906186030",
        "610124195805231422",
        "610526194704214033",
        "320981195602094007",
        "362202195307172328",
        "421127195111191571",
        "420881195504282111",
        "130532196207070027",
        "130921196107010817",
        "130921196010170181",
        "511028195809295317"
    )
}