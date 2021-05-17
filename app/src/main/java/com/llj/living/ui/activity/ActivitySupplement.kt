package com.llj.living.ui.activity

import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.llj.living.R
import com.llj.living.custom.ext.baseObserver
import com.llj.living.data.bean.EntAddonsBean
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivitySupplementBinding
import com.llj.living.databinding.ViewCheckHeaderBinding
import com.llj.living.logic.vm.SupplementPeopleVM
import com.llj.living.ui.adapter.SupplementDoingAdapter
import com.llj.living.ui.fragment.SuppleHadFragment
import com.llj.living.ui.fragment.SuppleWaitFragment
import com.llj.living.utils.StringUtils
import com.llj.living.utils.ToastUtils

class ActivitySupplement : BaseActivity<ActivitySupplementBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_supplement

    override fun setToolbar() = ToolbarConfig("补录信息", isShowBack = true, isShowMenu = false)

    override fun init() {

        val addonsId = intent.getIntExtra(SupplementDoingAdapter.SUPPLE_ID_FLAG, -1)

        val addonsBean =
            intent.getParcelableExtra<EntAddonsBean>(SupplementDoingAdapter.SUPPLE_BEAN_FLAG)

        if (addonsId == -1 || addonsBean == null) {
            ToastUtils.toastShort("数据解析错误 请返回重试")
            finish()
            return
        }

        val headerBinding =
            DataBindingUtil.findBinding<ViewCheckHeaderBinding>(getDataBinding().root.findViewById(R.id.header))
        headerBinding?.apply {
            tvWaitStr.text = resources.getString(R.string.wait_supple_count)
            tvHadStr.text = resources.getString(R.string.had_supple_count)
            startTime = StringUtils.convertMyTimeStr(addonsBean.start_at)
            endTime = StringUtils.convertMyTimeStr(addonsBean.end_at)
            hadCount = addonsBean.people_reput_count.toString()
            waitCount = (addonsBean.people_count).toString()

        }

        getDataBinding().viewPager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2

            override fun createFragment(position: Int): Fragment {
                return if (position == 0) SuppleWaitFragment.getInstance()
                else SuppleHadFragment.getInstance()
            }
        }

        getDataBinding().apply {
            viewPager2.isUserInputEnabled = false //禁用水平滑动
            TabLayoutMediator(TabLayout, viewPager2) { tab: TabLayout.Tab, position: Int ->
                if (position == 0) tab.text = resources.getString(R.string.wait_supple)
                else tab.text = resources.getString(R.string.had_supple)
            }.attach()
        }

    }

}