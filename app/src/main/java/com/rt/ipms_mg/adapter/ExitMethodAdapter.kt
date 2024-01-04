package com.rt.ipms_mg.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import com.rt.base.adapter.BaseBindingAdapter
import com.rt.base.adapter.VBViewHolder
import com.rt.base.bean.ExitMethodBean
import com.rt.ipms_mg.databinding.ItemParkingChooseStreetBinding

class ExitMethodAdapter(
    data: MutableList<ExitMethodBean>? = null,
    var currentMethod: ExitMethodBean?,
    val callback: ChooseExitMethodAdapterCallBack
) :
    BaseBindingAdapter<ExitMethodBean, ItemParkingChooseStreetBinding>(data) {
    var lastClassificationCB: CheckBox? = null
    var currentClassificationCB: CheckBox? = null
    override fun convert(holder: VBViewHolder<ItemParkingChooseStreetBinding>, item: ExitMethodBean) {
        holder.vb.tvStreet.text = item.name
        if (currentMethod != null && currentMethod!!.id == item.id) {
            holder.vb.cbStreet.isChecked = true
            currentClassificationCB = holder.vb.cbStreet
        }
        holder.vb.rlStreet.setOnClickListener {
            lastClassificationCB = currentClassificationCB
            lastClassificationCB?.isChecked = false
            currentClassificationCB = holder.vb.cbStreet
            currentClassificationCB?.isChecked = true
            currentMethod = item
            callback.chooseExitMethod(currentMethod!!)
        }
        holder.vb.cbStreet.setOnClickListener {
            lastClassificationCB = currentClassificationCB
            lastClassificationCB?.isChecked = false
            currentClassificationCB = holder.vb.cbStreet
            currentClassificationCB?.isChecked = true
            currentMethod = item
            callback.chooseExitMethod(currentMethod!!)
        }
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemParkingChooseStreetBinding {
        return ItemParkingChooseStreetBinding.inflate(inflater)
    }

    interface ChooseExitMethodAdapterCallBack {
        fun chooseExitMethod(method: ExitMethodBean)
    }
}