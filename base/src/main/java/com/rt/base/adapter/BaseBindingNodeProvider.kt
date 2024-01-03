package com.rt.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * Created by hy on 2022/6/1.
 */

abstract class BaseBindingNodeProvider<VB : ViewBinding>: BaseNodeProvider() {
    override val layoutId: Int
        get() = 0

    abstract fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): VB

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VBViewHolder<VB> {
        val viewBinding = createViewBinding(LayoutInflater.from(parent.context), parent)
        return VBViewHolder(viewBinding, viewBinding.root)
    }

}