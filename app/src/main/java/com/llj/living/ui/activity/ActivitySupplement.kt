package com.llj.living.ui.activity

import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.llj.living.R
import com.llj.living.custom.ext.toastShort
import com.llj.living.data.bean.AddonsByEntIdBean
import com.llj.living.data.bean.EntAddonsBean
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.data.database.SuppleDoing
import com.llj.living.databinding.ActivitySupplementBinding
import com.llj.living.databinding.ViewCheckHeaderBinding
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.ui.adapter.SuppleDoingAdapter
import com.llj.living.ui.adapter.SupplementDoingTestAdapter
import com.llj.living.ui.fragment.HadSuppleFragment
import com.llj.living.ui.fragment.WaitSuppleFragment
import com.llj.living.utils.LogUtils
import com.llj.living.utils.ToastUtils

class ActivitySupplement : BaseActivity<ActivitySupplementBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_supplement

    override fun init() {
        setToolbar(ToolbarConfig("补录信息", isShowBack = true, isShowMenu = false))

        val addonsId = intent.getIntExtra(SupplementDoingTestAdapter.SUPPLE_ID_FLAG, -1)

        val addonsBean =
            intent.getParcelableExtra<EntAddonsBean>(SupplementDoingTestAdapter.SUPPLE_BEAN_FLAG)

        if (addonsId == -1 || addonsBean == null) {
            ToastUtils.toastShort("数据解析错误 请返回重试")
            finish()
            return
        }

        val headerBinding =
            DataBindingUtil.findBinding<ViewCheckHeaderBinding>(getDataBinding().root.findViewById(R.id.check_header))
        headerBinding?.tvWaitStr?.text = resources.getString(R.string.wait_supple_count)
        headerBinding?.tvHadStr?.text = resources.getString(R.string.had_supple_count)
        headerBinding?.entAddons = addonsBean

        getDataBinding().viewPager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2

            override fun createFragment(position: Int): Fragment {
                return if (position == 0) WaitSuppleFragment.getInstance()
                else HadSuppleFragment.getInstance()
            }
        }

        getDataBinding().apply {
            viewPager2.isUserInputEnabled = false //禁用水平滑动
            TabLayoutMediator(TabLayout, viewPager2) { tab: TabLayout.Tab, position: Int ->
                if (position == 0) tab.text = resources.getString(R.string.wait_supple)
                else tab.text = resources.getString(R.string.had_supple)
            }.attach()
        }

        LogUtils.d("ActivitySupplement", "id:${SuppleDoingAdapter.id}")

    }

}