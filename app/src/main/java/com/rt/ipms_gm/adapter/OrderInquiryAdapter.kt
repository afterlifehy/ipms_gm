package com.rt.ipms_gm.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import com.rt.base.adapter.BaseBindingAdapter
import com.rt.base.adapter.VBViewHolder
import com.rt.base.bean.OrderBean
import com.rt.base.ext.i18n
import com.rt.common.util.AppUtil
import com.rt.common.util.BigDecimalManager
import com.rt.ipms_gm.databinding.ItemOrderBinding
import com.zrq.spanbuilder.TextStyle
import com.rt.base.ext.gone
import com.rt.base.ext.show

class OrderInquiryAdapter(data: MutableList<OrderBean>? = null, val onClickListener: OnClickListener) :
    BaseBindingAdapter<OrderBean, ItemOrderBinding>(data) {
    val colorsBlue = intArrayOf(
        com.rt.base.R.color.color_ff04a091,
        com.rt.base.R.color.color_ff04a091,
        com.rt.base.R.color.color_ff04a091
    )
    val colorsRed = intArrayOf(
        com.rt.base.R.color.color_ffe92404,
        com.rt.base.R.color.color_ffe92404,
        com.rt.base.R.color.color_ffe92404
    )
    val sizes = intArrayOf(16, 20, 16)
    val styles = arrayOf(TextStyle.NORMAL, TextStyle.BOLD, TextStyle.NORMAL)
    val colors2 = intArrayOf(com.rt.base.R.color.color_ff666666, com.rt.base.R.color.color_ff1a1a1a)
    val sizes2 = intArrayOf(19, 19)
    var orderList: MutableList<OrderBean> = ArrayList()

    override fun convert(holder: VBViewHolder<ItemOrderBinding>, item: OrderBean) {
        holder.vb.tvNum.text = AppUtil.fillZero((data.indexOf(item) + 1).toString())
        holder.vb.tvLicensePlate.text = item.carLicense
        if (item.paidAmount.toDouble() > 0.0 || (item.paidAmount.toDouble() == 0.0 && item.amount.toDouble() == 0.0)) {
            holder.vb.cbOrder.gone()
            val strings = arrayOf(
                i18n(com.rt.base.R.string.已付),
                AppUtil.keepNDecimals(item.paidAmount, 2),
                i18n(com.rt.base.R.string.元)
            )
            holder.vb.tvAmount.text = AppUtil.getSpan(strings, sizes, colorsBlue, styles)
        } else {
            if (item.isPrinted == "0") {
                holder.vb.cbOrder.show()
            } else {
                holder.vb.cbOrder.gone()
            }
            val strings = arrayOf(
                i18n(com.rt.base.R.string.欠),
                AppUtil.keepNDecimals(BigDecimalManager.subtractionDoubleToString(item.amount.toDouble(), item.paidAmount.toDouble()), 2),
                i18n(com.rt.base.R.string.元)
            )
            holder.vb.tvAmount.text = AppUtil.getSpan(strings, sizes, colorsRed, styles)
        }

        val strings2 = arrayOf(i18n(com.rt.base.R.string.入场) + ":", item.startTime)
        holder.vb.tvStartTime.text = AppUtil.getSpan(strings2, sizes2, colors2)
        val strings3 = arrayOf(i18n(com.rt.base.R.string.出场) + ":", item.endTime)
        holder.vb.tvEndTime.text = AppUtil.getSpan(strings3, sizes2, colors2)
        holder.vb.tvNo.text = item.parkingNo

        holder.vb.cbOrder.isChecked = false
        holder.vb.cbOrder.tag = item
        holder.vb.cbOrder.setOnCheckedChangeListener(object : OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if (p1) {
                    orderList.add(item)
                } else {
                    orderList.remove(item)
                }
            }
        })
        holder.vb.flOrder.tag = item
        holder.vb.flOrder.setOnClickListener(onClickListener)
    }


    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemOrderBinding {
        return ItemOrderBinding.inflate(inflater)
    }

    fun getUploadOrderList(): MutableList<OrderBean> {
        return orderList
    }

    fun clearUploadOrderList() {
        orderList.clear()
    }
}