package com.rt.ipms_mg.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import com.blankj.utilcode.util.SizeUtils
import com.rt.base.adapter.BaseBindingAdapter
import com.rt.base.adapter.VBViewHolder
import com.rt.base.bean.BlueToothDeviceBean
import com.rt.ipms_mg.databinding.ItemBluetoothDeviceBinding

class BlueToothDeviceAdapter(data: MutableList<BluetoothDevice>? = null, var currentDevice: BlueToothDeviceBean?) :
    BaseBindingAdapter<BluetoothDevice, ItemBluetoothDeviceBinding>(data) {
    var checkedDevice: BluetoothDevice? = null
    var lastDevice: BluetoothDevice? = null

    @SuppressLint("MissingPermission")
    override fun convert(holder: VBViewHolder<ItemBluetoothDeviceBinding>, item: BluetoothDevice) {
        holder.vb.tvDevice.text = item.name
        if (currentDevice != null && checkedDevice == null && item.address == currentDevice!!.address) {

            checkedDevice = item
        }
        if (item == checkedDevice) {
            holder.vb.cbDevice.isChecked = true
        } else {
            holder.vb.cbDevice.isChecked = false
        }
        holder.vb.rlDevice.setOnClickListener {
            holder.vb.cbDevice.isChecked = true
            if (holder.vb.cbDevice.isChecked) {
                lastDevice = checkedDevice
                checkedDevice = item
                notifyItemChanged(data.indexOf(lastDevice))
                notifyItemChanged(data.indexOf(checkedDevice))
            }
        }
        holder.vb.cbDevice.setOnClickListener {
            if (holder.vb.cbDevice.isChecked) {
                lastDevice = checkedDevice
                checkedDevice = item
                notifyItemChanged(data.indexOf(lastDevice))
                notifyItemChanged(data.indexOf(checkedDevice))
            } else {
                lastDevice = checkedDevice
                checkedDevice = null
            }
        }
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemBluetoothDeviceBinding {
        val lp = ViewGroup.MarginLayoutParams(WindowManager.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(60f))
        val binding = ItemBluetoothDeviceBinding.inflate(inflater)
        binding.root.layoutParams = lp
        return binding
    }

//    fun getCheckedList(): MutableList<Int> {
//        return checkedList
//    }
}