package com.llj.living.ui.activity

import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.llj.living.R
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.data.database.SuppleDoing
import com.llj.living.databinding.ActivitySupplementBinding
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.ui.adapter.SuppleDoingAdapter
import com.llj.living.ui.fragment.HadSuppleFragment
import com.llj.living.ui.fragment.WaitSuppleFragment
import com.llj.living.utils.LogUtils

class ActivitySupplement : BaseActivity<ActivitySupplementBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_supplement

    private val dbViewModel by viewModels<DatabaseVM>()

    private lateinit var pagedListLives: LiveData<SuppleDoing>

    override fun init() {
        setToolbar(ToolbarConfig("补录信息", isShowBack = true, isShowMenu = false))

        getDataBinding().root.findViewById<TextView>(R.id.tv_wait_str).text =
            resources.getString(R.string.wait_supple_count)
        getDataBinding().root.findViewById<TextView>(R.id.tv_had_str).text =
            resources.getString(R.string.had_supple_count)

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

        LogUtils.d("ActivitySupplement","id:${SuppleDoingAdapter.id}")
        pagedListLives = dbViewModel.getSuppleInfoItemListLD(SuppleDoingAdapter.id)

        pagedListLives.observe(this, Observer { data ->
            LogUtils.d("ActivitySupplement","observe:--->")
            data?.let {
                waitSuppleCount = it.waitDealWith
                hadSuppleCount = it.hadDealWith
                LogUtils.d("ActivitySupplement","data:--->")
                getDataBinding().root.findViewById<TextView>(R.id.tvStartTime).text = it.startTime
                getDataBinding().root.findViewById<TextView>(R.id.tvEndTime).text = it.endTime
                getDataBinding().root.findViewById<TextView>(R.id.tvHeaderWait).text =
                    it.waitDealWith.toString()
                getDataBinding().root.findViewById<TextView>(R.id.tvHeaderHad).text =
                    it.hadDealWith.toString()
            }
        })
    }

    companion object{
        var waitSuppleCount = -1
        var hadSuppleCount = -1
    }
}