package com.rt.ipms_gm.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.rt.base.adapter.BaseBindingAdapter
import com.rt.base.adapter.VBViewHolder
import com.rt.base.bean.TicketPrintBean
import com.rt.base.bean.TicketPrintResultBean
import com.rt.base.bean.TransactionBean
import com.rt.ipms_gm.databinding.ItemTransactionRecordBinding

class TransactionRecordAdapter(data: MutableList<TicketPrintBean>? = null, val onClickListener: OnClickListener) :
    BaseBindingAdapter<TicketPrintBean, ItemTransactionRecordBinding>(data) {
    override fun convert(holder: VBViewHolder<ItemTransactionRecordBinding>, item: TicketPrintBean) {
        holder.vb.tvOrderNo.text = item.tradeNo
        holder.vb.tvAmount.text = "${item.payMoney}元"
        holder.vb.tvStartTime.text = item.startTime
        holder.vb.tvEndTime.text = item.endTime
        holder.vb.flNotification.tag = item
        holder.vb.flNotification.setOnClickListener(onClickListener)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemTransactionRecordBinding {
        return ItemTransactionRecordBinding.inflate(inflater)
    }
}