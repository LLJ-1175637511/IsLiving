package com.llj.living.ui.activity

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.llj.living.R
import com.llj.living.data.bean.ToolbarConfig
import com.llj.living.databinding.ActivitySupplementBinding
import com.llj.living.ui.fragment.HadSuppleFragment
import com.llj.living.ui.fragment.WaitSuppleFragment

class ActivitySupplement:BaseActivity<ActivitySupplementBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_supplement

    override fun init() {
        setToolbar(ToolbarConfig("补录信息",isShowBack = true,isShowMenu = false))

        getDataBinding().root.findViewById<TextView>(R.id.tv_wait_str).text = resources.getString(R.string.wait_supple_count)
        getDataBinding().root.findViewById<TextView>(R.id.tv_had_str).text = resources.getString(R.string.had_supple_count)

        getDataBinding().viewPager2.adapter = object :FragmentStateAdapter(this){
            override fun getItemCount() = 2

            override fun createFragment(position: Int): Fragment {
                return if (position == 0) WaitSuppleFragment.getInstance()
                else HadSuppleFragment.getInstance()
            }

        }

        getDataBinding().apply {
            TabLayoutMediator( TabLayout,viewPager2){ tab: TabLayout.Tab, position: Int ->
                if (position == 0) tab.text = resources.getString(R.string.wait_supple)
                else tab.text = resources.getString(R.string.had_supple)
            }.attach()
        }
    }

}