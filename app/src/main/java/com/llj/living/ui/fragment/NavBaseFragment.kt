package com.llj.living.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class NavBaseFragment<DB : ViewDataBinding> : Fragment() {

    val TAG = this.javaClass.simpleName

    abstract fun getLayoutId(): Int

    private lateinit var mDataBinding: DB

    open fun getBinding() = mDataBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate<DB>(inflater, getLayoutId(), container, false)
        mDataBinding.lifecycleOwner = if (setLifeOwnerIsActivity()) requireActivity()
        else this
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    abstract fun init()

    open fun setLifeOwnerIsActivity() = false

    override fun onDestroy() {
        super.onDestroy()
        if (this::mDataBinding.isInitialized) mDataBinding.unbind()
    }

}