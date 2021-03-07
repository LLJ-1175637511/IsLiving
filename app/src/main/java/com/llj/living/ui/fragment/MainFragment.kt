package com.llj.living.ui.fragment

import androidx.navigation.Navigation
import com.llj.living.R
import com.llj.living.databinding.FragmentMainBinding
import com.llj.living.logic.vm.MainFragmentVM

class MainFragment : NavBaseFragment<FragmentMainBinding>() {

    private lateinit var viewModel: MainFragmentVM

    override fun getLayoutId(): Int = R.layout.fragment_main

//    private lateinit var navController: NavController

    override fun init() {
        getBinding().supplementLayout.setOnClickListener {
            val navController = Navigation.findNavController(it)
            navController.navigate(R.id.navigation_supplement)
        }
        getBinding().checkLayout.setOnClickListener {
            val navController = Navigation.findNavController(it)
            navController.navigate(R.id.navigation_check)
        }
    }

    override fun setLifeOwnerIsActivity(): Boolean = true

}