package com.llj.living.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider

abstract class NavBaseFragment<DB : ViewDataBinding> : Fragment() {

    abstract fun getLayoutId(): Int

    private lateinit var mDataBinding: DB

    open fun getBinding() = mDataBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate<DB>(inflater, getLayoutId(), container, false)
        return mDataBinding.root
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
            ViewModelProvider(this, SavedStateViewModelFactory(requireActivity().application, requireActivity()))[vm]
        } else {
            ViewModelProvider(this, SavedStateViewModelFactory(requireActivity().application, this))[vm]
        }

    inline fun <reified VM : AndroidViewModel> initViewModel() = initViewModel(VM::class.java)

    open fun setLifeOwnerIsActivity() = false

}