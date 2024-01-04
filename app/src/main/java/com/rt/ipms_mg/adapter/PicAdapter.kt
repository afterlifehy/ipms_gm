package com.rt.ipms_mg.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.blankj.utilcode.util.SizeUtils
import com.rt.base.adapter.BaseBindingAdapter
import com.rt.base.adapter.VBViewHolder
import com.rt.ipms_mg.databinding.ItemPicBinding
import com.rt.common.util.GlideUtils

class PicAdapter(data: MutableList<String>? = null, val onClickListener: OnClickListener) :
    BaseBindingAdapter<String, ItemPicBinding>(data) {
    override fun convert(holder: VBViewHolder<ItemPicBinding>, item: String) {
        val lp = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        lp.bottomMargin = SizeUtils.dp2px(20f)
        holder.vb.rivPic.layoutParams = lp

        GlideUtils.instance?.loadImage(holder.vb.rivPic, item, com.rt.common.R.mipmap.ic_placeholder_2)
        holder.vb.rivPic.tag = item
        holder.vb.rivPic.setOnClickListener(onClickListener)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemPicBinding {
        return ItemPicBinding.inflate(inflater)
    }
}