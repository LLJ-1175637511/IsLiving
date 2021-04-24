package com.llj.living.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.llj.living.R
import com.llj.living.data.database.CheckDoing
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.utils.LogUtils

class CheckFragment :BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_check

    lateinit var tabLayout:TabLayout
    lateinit var viewPager2:ViewPager2
    private val dbViewModel by activityViewModels<DatabaseVM>()
    override fun init() {
        tabLayout = requireView().findViewById(R.id.tab_layout_check)
        viewPager2 = requireView().findViewById(R.id.viewpager2_check)

        viewPager2.isUserInputEnabled = false    //设置水平不可滑动

        viewPager2.adapter = object :FragmentStateAdapter(requireActivity()){
            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                return if (position==0) CheckDoingFragment()
                else CheckFinishedFragment()
            }
        }

        TabLayoutMediator(tabLayout,viewPager2){ tab: TabLayout.Tab, position: Int ->
            if (position==0) tab.text = resources.getString(R.string.doing)
            else tab.text = resources.getString(R.string.finished)
        }.attach()

        dbViewModel.getSuppleFinishedLD().observe(this, Observer { suppleFinished->
            val tempList = mutableListOf<CheckDoing>()
            suppleFinished?.forEach {
                LogUtils.d("CheckFragment",it.toString())
                tempList.add(CheckDoing(
                    title = it.title,
                    endTime = it.endTime,
                    startTime = it.startTime,
                    hadDealWith = it.waitDealWith,
                    waitDealWith = it.hadDealWith
                ))
            }
            dbViewModel.insertCheckDoing(tempList)
        })
    }
}