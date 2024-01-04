package com.rt.ipms_mg.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.rt.base.adapter.BaseBindingAdapter
import com.rt.base.adapter.VBViewHolder
import com.rt.base.bean.DebtCollectionBean
import com.rt.common.util.AppUtil
import com.rt.ipms_mg.databinding.ItemDebtCollectionBinding
import com.zrq.spanbuilder.TextStyle

class DebtCollectionAdapter(data: MutableList<DebtCollectionBean>? = null, val onClickListener: OnClickListener) :
    BaseBindingAdapter<DebtCollectionBean, ItemDebtCollectionBinding>(data) {
    val colors = intArrayOf(com.rt.base.R.color.color_ffe92404, com.rt.base.R.color.color_ffe92404, com.rt.base.R.color.color_ffe92404)
    val sizes = intArrayOf(16, 20, 16)
    val styles = arrayOf(TextStyle.NORMAL, TextStyle.BOLD, TextStyle.NORMAL)
    val colors2 = intArrayOf(com.rt.base.R.color.color_ff666666, com.rt.base.R.color.color_ff1a1a1a)
    val sizes2 = intArrayOf(19, 19)

    override fun convert(holder: VBViewHolder<ItemDebtCollectionBinding>, item: DebtCollectionBean) {
        holder.vb.tvNum.text = AppUtil.fillZero((data.indexOf(item) + 1).toString())
        holder.vb.tvLicensePlate.text = item.carLicense
        val strings = arrayOf("欠：", "${AppUtil.keepNDecimal(item.oweMoney / 100.00,2)}", "元")
        holder.vb.tvAmount.text = AppUtil.getSpan(strings, sizes, colors, styles)
        val strings2 = arrayOf("入场：", item.startTime)
        holder.vb.tvStartTime.text = AppUtil.getSpan(strings2, sizes2, colors2)
        val strings3 = arrayOf("出场：", item.endTime)
        holder.vb.tvEndTime.text = AppUtil.getSpan(strings3, sizes2, colors2)
        holder.vb.tvAddress.text = item.streetName
        holder.vb.tvNo.text = item.parkingNo

        holder.vb.rrlDebtCollection.tag = item
        holder.vb.rrlDebtCollection.setOnClickListener(onClickListener)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemDebtCollectionBinding {
        return ItemDebtCollectionBinding.inflate(inflater)
    }
}