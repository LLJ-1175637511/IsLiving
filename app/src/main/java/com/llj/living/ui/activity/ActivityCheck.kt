package com.llj.living.ui.activity

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.llj.living.R
import com.llj.living.data.bean.EntCheckBean
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivityCheckBinding
import com.llj.living.databinding.ViewCheckHeaderBinding
import com.llj.living.ui.adapter.CheckDoingAdapter
import com.llj.living.ui.fragment.CheckHadFragment
import com.llj.living.ui.fragment.CheckWaitFragment
import com.llj.living.utils.StringUtils
import com.llj.living.utils.ToastUtils

class ActivityCheck : BaseActivity<ActivityCheckBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_check

    override fun init() {
        setToolbar(ToolbarConfig("核查信息", isShowBack = true, isShowMenu = true))

        val checkId = intent.getIntExtra(CheckDoingAdapter.CHECK_ID_FLAG, -1)

        val checkBean =
            intent.getParcelableExtra<EntCheckBean>(CheckDoingAdapter.CHECK_BEAN_FLAG)

        if (checkId == -1 || checkBean == null) {
            ToastUtils.toastShort("数据解析错误 请返回重试")
            finish()
            return
        }

        val headerBinding =
            DataBindingUtil.findBinding<ViewCheckHeaderBinding>(getDataBinding().root.findViewById(R.id.header))

        headerBinding?.apply {
            tvWaitStr.text = resources.getString(R.string.wait_supple_count)
            tvHadStr.text = resources.getString(R.string.had_supple_count)
            startTime = StringUtils.convertMyTimeStr(checkBean.start_at)
            endTime = StringUtils.convertMyTimeStr(checkBean.end_at)
            waitCount = (checkBean.people_count - checkBean.people_check_count).toString()
            hadCount = checkBean.people_check_count.toString()
        }

        getDataBinding().viewPager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2

            override fun createFragment(position: Int): Fragment {
                return if (position == 0) CheckWaitFragment.getInstance()
                else CheckHadFragment.getInstance()
            }
        }

        getDataBinding().apply {
            viewPager2.isUserInputEnabled = false //禁用水平滑动
            TabLayoutMediator(TabLayout, viewPager2) { tab: TabLayout.Tab, position: Int ->
                if (position == 0) tab.text = resources.getString(R.string.wait_check)
                else tab.text = resources.getString(R.string.had_check)
            }.attach()
        }
    }

}