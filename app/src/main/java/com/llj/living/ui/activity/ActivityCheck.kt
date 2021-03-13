package com.llj.living.ui.activity

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.llj.living.R
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivityCheckBinding
import com.llj.living.ui.fragment.HadCheckFragment
import com.llj.living.ui.fragment.WaitCheckFragment

class ActivityCheck:BaseActivity<ActivityCheckBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_check

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
            TabLayoutMediator( TabLayout,viewPager2){ tab: TabLayout.Tab, position: Int ->
                if (position == 0) tab.text = resources.getString(R.string.wait_check)
                else tab.text = resources.getString(R.string.had_check)
            }.attach()
        }
    }

}