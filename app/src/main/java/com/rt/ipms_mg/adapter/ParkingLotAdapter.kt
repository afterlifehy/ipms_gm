package com.rt.ipms_mg.adapter

import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.rt.base.BaseApplication
import com.rt.base.adapter.BaseBindingAdapter
import com.rt.base.adapter.VBViewHolder
import com.rt.base.bean.ParkingLotBean
import com.rt.base.ext.gone
import com.rt.base.ext.hide
import com.rt.base.ext.i18n
import com.rt.base.ext.show
import com.rt.ipms_mg.databinding.ItemParkingLotBinding
import com.rt.common.util.AppUtil
import com.rt.common.util.Constant

class ParkingLotAdapter(data: MutableList<ParkingLotBean>? = null, val onClickListener: OnClickListener) :
    BaseBindingAdapter<ParkingLotBean, ItemParkingLotBinding>(data) {
    var plateBgMap: MutableMap<String, Int> = ArrayMap()
    var plateTxtColorMap: MutableMap<String, Int> = ArrayMap()
    var colors = intArrayOf(com.rt.base.R.color.color_ffeb0000, com.rt.base.R.color.black)
    var colors2 = intArrayOf(com.rt.base.R.color.black, com.rt.base.R.color.color_ffeb0000)
    var sizes = intArrayOf(24, 24)

    init {
        plateBgMap[Constant.BLACK] = com.rt.common.R.mipmap.ic_plate_bg_black
        plateBgMap[Constant.WHITE] = com.rt.common.R.mipmap.ic_plate_bg_white
        plateBgMap[Constant.GREY] = com.rt.common.R.mipmap.ic_plate_bg_white
        plateBgMap[Constant.RED] = com.rt.common.R.mipmap.ic_plate_bg_white
        plateBgMap[Constant.BLUE] = com.rt.common.R.mipmap.ic_plate_bg_blue
        plateBgMap[Constant.YELLOW] = com.rt.common.R.mipmap.ic_plate_bg_yellow
        plateBgMap[Constant.ORANGE] = com.rt.common.R.mipmap.ic_plate_bg_white
        plateBgMap[Constant.BROWN] = com.rt.common.R.mipmap.ic_plate_bg_white
        plateBgMap[Constant.GREEN] = com.rt.common.R.mipmap.ic_plate_bg_green
        plateBgMap[Constant.PURPLE] = com.rt.common.R.mipmap.ic_plate_bg_white
        plateBgMap[Constant.CYAN] = com.rt.common.R.mipmap.ic_plate_bg_white
        plateBgMap[Constant.PINK] = com.rt.common.R.mipmap.ic_plate_bg_white
        plateBgMap[Constant.TRANSPARENT] = com.rt.common.R.mipmap.ic_plate_bg_white
        plateBgMap[Constant.OTHERS] = com.rt.common.R.mipmap.ic_plate_bg_white

        plateTxtColorMap[Constant.BLACK] = com.rt.base.R.color.white
        plateTxtColorMap[Constant.WHITE] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.GREY] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.RED] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.BLUE] = com.rt.base.R.color.white
        plateTxtColorMap[Constant.YELLOW] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.ORANGE] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.BROWN] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.GREEN] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.PURPLE] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.CYAN] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.PINK] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.TRANSPARENT] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.YELLOW_GREEN] = com.rt.base.R.color.black
        plateTxtColorMap[Constant.OTHERS] = com.rt.base.R.color.black
    }

    override fun convert(holder: VBViewHolder<ItemParkingLotBinding>, item: ParkingLotBean) {
        if (item.state == "01") {
            holder.vb.llPlate.hide()
            holder.vb.tvPlate.show()
            holder.vb.llParkingLotBg.setBackgroundResource(com.rt.common.R.mipmap.ic_parking_bg_grey)
            holder.vb.tvParkingLotNum.setBackgroundResource(com.rt.common.R.mipmap.ic_parking_num_bg_grey)
            holder.vb.tvPlate.text = i18n(com.rt.base.R.string.空闲)
            holder.vb.tvPlate.setTextColor(ContextCompat.getColor(BaseApplication.instance(), com.rt.base.R.color.black))
            holder.vb.tvPlate.background = null
            holder.vb.rflParking.tag = item
            holder.vb.rflParking.setOnClickListener(onClickListener)
        } else {
            if (item.deadLine > System.currentTimeMillis()) {
                holder.vb.llParkingLotBg.setBackgroundResource(com.rt.common.R.mipmap.ic_parking_bg_green)
                holder.vb.tvParkingLotNum.setBackgroundResource(com.rt.common.R.mipmap.ic_parking_num_bg_green)
            } else {
                holder.vb.llParkingLotBg.setBackgroundResource(com.rt.common.R.mipmap.ic_parking_bg_red)
                holder.vb.tvParkingLotNum.setBackgroundResource(com.rt.common.R.mipmap.ic_parking_num_bg_red)
            }
            if (item.carColor == "20") {
                holder.vb.llPlate.show()
                holder.vb.tvPlate.gone()
                holder.vb.tvPlate1.text = item.carLicense.substring(0, 2)
                holder.vb.tvPlate2.text = item.carLicense.substring(2, item.carLicense.length)
                holder.vb.tvPlate1.setTextColor(ContextCompat.getColor(BaseApplication.instance(), com.rt.base.R.color.black))
                holder.vb.tvPlate2.setTextColor(ContextCompat.getColor(BaseApplication.instance(), com.rt.base.R.color.black))
            } else {
                holder.vb.llPlate.hide()
                holder.vb.tvPlate.show()
                if (item.carLicense.startsWith("WJ")) {
                    val strings = arrayOf("WJ", item.carLicense.substring(2, item.carLicense.length))
                    holder.vb.tvPlate.text = AppUtil.getSpan(strings, sizes, colors)
                } else if (item.carLicense.contains("警")) {
                    val strings = arrayOf(item.carLicense.substring(0, item.carLicense.length - 1), "警")
                    holder.vb.tvPlate.text = AppUtil.getSpan(strings, sizes, colors2)
                } else {
                    holder.vb.tvPlate.text = item.carLicense
                }
                holder.vb.tvPlate.setTextColor(ContextCompat.getColor(BaseApplication.instance(), plateTxtColorMap[item.carColor]!!))
                holder.vb.tvPlate.background = plateBgMap[item.carColor]?.let { ContextCompat.getDrawable(BaseApplication.instance(), it) }
            }
            holder.vb.rflParking.tag = item
            holder.vb.rflParking.setOnClickListener(onClickListener)
        }
        holder.vb.tvParkingLotNum.text = item.parkingNo.substring(item.parkingNo.length - 3, item.parkingNo.length)
    }

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemParkingLotBinding {
        return ItemParkingLotBinding.inflate(inflater)
    }
}