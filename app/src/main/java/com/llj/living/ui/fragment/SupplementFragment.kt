package com.llj.living.ui.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.llj.living.R

class SupplementFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_supplement

    lateinit var viewPager2: ViewPager2
    lateinit var tabLayout: TabLayout

    override fun init() {
        viewPager2 = requireView().findViewById(R.id.viewpager2_supplement)
        tabLayout = requireView().findViewById(R.id.tab_layout_supplement)

        viewPager2.adapter = object : FragmentStateAdapter(requireActivity()) {
            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                return if (position == 0) SuppleDoingFragment.getInstance()
                else SuppleFinishFragment.getInstance()
            }
        }

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            if (position == 0) tab.text = resources.getString(R.string.doing)
            else tab.text = resources.getString(R.string.finished)
        }.attach() //沙雕 不加attach()是不会显示顶部文字的

    }


}