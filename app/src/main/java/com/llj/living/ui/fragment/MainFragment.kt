package com.llj.living.ui.fragment

import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.data.bean.AdBean
import com.llj.living.databinding.FragmentNewMainBinding
import com.llj.living.logic.vm.MainFragmentVM
import com.llj.living.ui.adapter.MainAdAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainFragment : NavBaseFragment<FragmentNewMainBinding>() {

    private lateinit var viewModel: MainFragmentVM

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
        repeat(10){
            list.add(AdBean("标题${(0..100).random()}",it))
        }
        adapter.submitList(list)
    }

    override fun setLifeOwnerIsActivity(): Boolean = true

}