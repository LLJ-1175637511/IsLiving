package com.llj.living.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider

abstract class BaseFragment : Fragment() {

    abstract fun getLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    abstract fun init()

    /**
     * 便捷初始化viewModel
     */
    fun <VM : AndroidViewModel> initViewModel(vm: Class<VM>) =
        if (setLifeOwnerIsActivity()) { //设置viewModel持有者是activity还是fragment
            ViewModelProvider(
                this,
                SavedStateViewModelFactory(requireActivity().application, requireActivity())
            )[vm]
        } else {
            ViewModelProvider(
                this,
                SavedStateViewModelFactory(requireActivity().application, this)
            )[vm]
        }

    inline fun <reified VM : AndroidViewModel> initViewModel() = initViewModel(VM::class.java)

    open fun setLifeOwnerIsActivity() = false

}