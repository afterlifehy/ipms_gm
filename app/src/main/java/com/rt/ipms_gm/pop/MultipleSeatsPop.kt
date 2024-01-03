package com.rt.ipms_gm.pop

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import com.rt.base.ext.gone
import com.rt.ipms_gm.R
import com.rt.ipms_gm.databinding.PopMultipleSeatsBinding
import com.rt.common.util.GlideUtils

/**
 * Created by huy  on 2022/12/7.
 */
class MultipleSeatsPop(
    val context: Context?,
    var currentParkingNo: String,
    var multipleSeat: String,
    var parkingAmount: Int,
    var callback: MultipleSeatsCallback
) :
    PopupWindow(context), View.OnClickListener {

    private lateinit var binding: PopMultipleSeatsBinding
    var frontSeat: String = ""
    var behindSeat: String = ""

    init {
        initView()
    }

    private fun initView() {
        binding = PopMultipleSeatsBinding.inflate(LayoutInflater.from(context))

        frontSeat = (currentParkingNo.toInt() - 1).toString()
        behindSeat = (currentParkingNo.toInt() + 1).toString()
        if (currentParkingNo.toInt() == 1) {
            binding.rrlParkingNo.gone()
            binding.viewLiner.gone()
            binding.tvParkingNo2.text = behindSeat
        } else if (currentParkingNo.toInt() == parkingAmount) {
            binding.rrlParkingNo2.gone()
            binding.viewLiner2.gone()
            binding.tvParkingNo.text = frontSeat
        } else {
            binding.tvParkingNo.text = frontSeat
            binding.tvParkingNo2.text = behindSeat
        }

        when (multipleSeat) {
            "" -> {
                GlideUtils.instance?.loadImage(binding.ivParkingNoNone, com.rt.common.R.mipmap.ic_parking_street_checked)
            }

            frontSeat -> {
                GlideUtils.instance?.loadImage(binding.ivParkingNo, com.rt.common.R.mipmap.ic_parking_street_checked)
            }

            behindSeat -> {
                GlideUtils.instance?.loadImage(binding.ivParkingNo2, com.rt.common.R.mipmap.ic_parking_street_checked)
            }
        }
        binding.rrlParkingNoNone.setOnClickListener(this)
        binding.rrlParkingNo.setOnClickListener(this)
        binding.rrlParkingNo2.setOnClickListener(this)

        contentView = binding.root
        contentView!!.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY)
        this.width = ViewGroup.LayoutParams.WRAP_CONTENT
        this.height = ViewGroup.LayoutParams.WRAP_CONTENT
        this.isFocusable = true
        this.isOutsideTouchable = true
        val dw = ColorDrawable(-0)
        //设置弹出窗体的背景
        this.setBackgroundDrawable(dw)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rrl_parkingNoNone -> {
                callback.selecctSeats("")
                dismiss()
            }

            R.id.rrl_parkingNo -> {
                callback.selecctSeats(frontSeat)
                dismiss()
            }

            R.id.rrl_parkingNo2 -> {
                callback.selecctSeats(behindSeat)
                dismiss()
            }
        }
    }

    interface MultipleSeatsCallback {
        fun selecctSeats(seat: String)
    }


}