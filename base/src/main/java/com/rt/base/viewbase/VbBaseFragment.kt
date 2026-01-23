package com.rt.base.viewbase

import android.view.View
import androidx.viewbinding.ViewBinding
import com.rt.base.R
import com.rt.base.base.mvvm.BaseViewModel

abstract class VbBaseFragment<VM : BaseViewModel, vb : ViewBinding> : BaseDataFragmentKt<VM>() {
    lateinit var binding: vb

    override fun getBindingView(): View? {
        val mBindind = getVbBindingView()
        binding = mBindind as vb
        return mBindind.root
    }


    abstract fun getVbBindingView(): ViewBinding

    override fun getLayoutResId(): Int {
        return R.layout.activity_vb_default_layout
    }
}