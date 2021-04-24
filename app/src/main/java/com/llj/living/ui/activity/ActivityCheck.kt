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
import com.llj.living.data.database.CheckDoing
import com.llj.living.databinding.ActivityCheckBinding
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.ui.adapter.CheckDoingAdapter
import com.llj.living.ui.fragment.HadCheckFragment
import com.llj.living.ui.fragment.WaitCheckFragment
import com.llj.living.utils.LogUtils

class ActivityCheck:BaseActivity<ActivityCheckBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_check


    private val dbViewModel by viewModels<DatabaseVM>()

    private lateinit var pagedListLives: LiveData<CheckDoing>

    override fun init() {
        setToolbar(ToolbarConfig("核查信息",isShowBack = true,isShowMenu = true))

        getDataBinding().viewPager2.adapter = object :FragmentStateAdapter(this){
            override fun getItemCount() = 2

            override fun createFragment(position: Int): Fragment {
                return if (position == 0) WaitCheckFragment.getInstance()
                else HadCheckFragment.getInstance()
            }
        }

        getDataBinding().apply {
            viewPager2.isUserInputEnabled = false //禁用水平滑动
            TabLayoutMediator( TabLayout,viewPager2){ tab: TabLayout.Tab, position: Int ->
                if (position == 0) tab.text = resources.getString(R.string.wait_check)
                else tab.text = resources.getString(R.string.had_check)
            }.attach()
        }

        pagedListLives = dbViewModel.getCheckDoingItemById(CheckDoingAdapter.id)

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