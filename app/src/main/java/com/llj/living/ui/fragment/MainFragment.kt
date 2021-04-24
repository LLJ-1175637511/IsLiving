package com.llj.living.ui.fragment

import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.data.bean.AdBean
import com.llj.living.databinding.FragmentNewMainBinding
import com.llj.living.ui.adapter.MainAdAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainFragment : NavBaseFragment<FragmentNewMainBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_new_main

    private val adapter by lazy { MainAdAdapter() }

//    private lateinit var navController: NavController

    override fun init() {
       /* getBinding().supplementLayout.setOnClickListener {
            val navController = Navigation.findNavController(it)
            navController.navigate(R.id.navigation_supplement)
        }
        getBinding().checkLayout.setOnClickListener {
            val navController = Navigation.findNavController(it)
            navController.navigate(R.id.navigation_check)
        }*/
        getBinding().recyclerViewMain.adapter = adapter

        loadAdData()

        getBinding().refreshLayoutMain.apply {
            setOnRefreshListener {
                lifecycleScope.launch {
                    isRefreshing = true
                    delay(500)
                    loadAdData()
                    isRefreshing = false
                }
            }
        }
    }

    private fun loadAdData(){
        val list = mutableListOf<AdBean>()
        list.add(AdBean("补录12周第1批",1,"南苑B区（5-13号房）"))
        list.add(AdBean("补录13周第2批",2,"北苑A区（2-4号房）"))
        list.add(AdBean("补录13周第2批",3,"北苑B区（3-6号房）"))
        list.add(AdBean("补录14周第1批",4,"北苑A区（1-3号房）"))
        adapter.submitList(list)
    }

    override fun setLifeOwnerIsActivity(): Boolean = true

}