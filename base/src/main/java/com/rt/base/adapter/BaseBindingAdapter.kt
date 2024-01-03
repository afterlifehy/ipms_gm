package com.rt.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Created by hy on 2022/5/31.
 */
class VBViewHolder<VB : ViewBinding>(val vb: VB, view: View) : BaseViewHolder(view)

abstract class BaseBindingAdapter<T, VB : ViewBinding>(data: MutableList<T>? = null) : BaseQuickAdapter<T, VBViewHolder<VB>>(0, data) {
    internal var mRecyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    abstract fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): VB

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VBViewHolder<VB> {
        val viewBinding = createViewBinding(LayoutInflater.from(parent.context), parent)
        return VBViewHolder(viewBinding, viewBinding.root)
    }

    fun setEmptyView(layoutResId: Int, img: Int, tips: String) {
        mRecyclerView?.let {
            val view = LayoutInflater.from(it.context).inflate(layoutResId, it, false)
//            GlideUtil.loadImagePreview(img, view.findViewById(R.id.iv_img))
//            view.findViewById<TextView>(R.id.tv_noData).text = tips
            setEmptyView(view)
        }
    }
}