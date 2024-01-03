package com.rt.base.adapter

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Created by hy on 2021/2/19.
 */
abstract class CustomBaseQuickAdapter<T, VH : BaseViewHolder>(layoutResId: Int, data: MutableList<T>?) : BaseQuickAdapter<T, VH>(layoutResId, data) {
    internal var mRecyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
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