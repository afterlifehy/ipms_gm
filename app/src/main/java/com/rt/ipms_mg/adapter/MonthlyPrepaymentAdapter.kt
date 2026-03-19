package com.rt.ipms_mg.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.rt.base.R
import com.rt.base.adapter.BaseBindingAdapter
import com.rt.base.adapter.VBViewHolder
import com.rt.base.bean.OrderBean
import com.rt.base.ext.i18n
import com.rt.common.util.AppUtil
import com.rt.ipms_mg.databinding.ItemMonthlyPrepaymentBinding

class MonthlyPrepaymentAdapter(data: MutableList<OrderBean>? = null, val onclick: (order: OrderBean) -> Unit) :
    BaseBindingAdapter<OrderBean, ItemMonthlyPrepaymentBinding>(data) {

    override fun convert(holder: VBViewHolder<ItemMonthlyPrepaymentBinding>, item: OrderBean) {
        holder.vb.tvNum.text = AppUtil.fillZero((data.indexOf(item) + 1).toString())
        holder.vb.tvLicensePlate.text = item.carLicense
        
        // 显示金额
        holder.vb.tvAmount.text = AppUtil.keepNDecimals(item.amount, 2) + "元"
        
        // 显示道路名称
        holder.vb.tvRoadName.text = item.streetName
        
        // 显示申请时间
        holder.vb.tvApplyTime.text = "申请时间：" + item.startTime
        
        // 根据状态显示不同颜色
//        if (item.orderStatus == "1") {
//            holder.vb.tvStatus.text = "审核通过"
//            holder.vb.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.color_ff04a091))
//        } else if (item.orderStatus == "2") {
//            holder.vb.tvStatus.text = "待审核"
//            holder.vb.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.color_fff38d00))
//        } else if (item.orderStatus == "3") {
//            holder.vb.tvStatus.text = "审核拒绝"
//            holder.vb.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.color_ffe92404))
//        } else {
//            holder.vb.tvStatus.text = "未知状态"
//            holder.vb.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.color_ff666666))
//        }

        holder.vb.flMonthlyPrepayment.tag = item
        holder.vb.flMonthlyPrepayment.setOnClickListener{
            onclick(item)
        }
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemMonthlyPrepaymentBinding {
        return ItemMonthlyPrepaymentBinding.inflate(inflater)
    }
}
