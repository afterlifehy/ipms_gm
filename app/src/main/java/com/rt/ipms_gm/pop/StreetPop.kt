package com.rt.ipms_gm.pop

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.rt.base.BaseApplication
import com.rt.base.bean.Street
import com.rt.ipms_gm.R
import com.rt.ipms_gm.databinding.PopStreetBinding
import com.rt.ipms_gm.adapter.ParkingChooseStreetAdapter

/**
 * Created by huy  on 2022/12/7.
 */
class StreetPop(val context: Context?, var currentStreet: Street?, val streetList: MutableList<Street>, var callback: StreetSelectCallBack) :
    PopupWindow(context), View.OnClickListener {

    private lateinit var binding: PopStreetBinding
    var parkingChooseStreetAdapter: ParkingChooseStreetAdapter? = null
    var selectedStreet: Street? = null

    init {
        initView()
    }

    private fun initView() {
        binding = PopStreetBinding.inflate(LayoutInflater.from(context))
        binding.rvStreet.setHasFixedSize(true)
        binding.rvStreet.layoutManager = LinearLayoutManager(BaseApplication.instance())
        parkingChooseStreetAdapter =
            ParkingChooseStreetAdapter(streetList, currentStreet!!.copy(), object : ParkingChooseStreetAdapter.ChooseStreetAdapterCallBack {
                override fun chooseStreet(street: Street) {
                    selectedStreet = street
                }
            })
        binding.rvStreet.adapter = parkingChooseStreetAdapter

        binding.rtvOk.setOnClickListener(this)
        binding.viewMask.setOnClickListener(this)

        contentView = binding.root
        contentView!!.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY)
        this.width = ViewGroup.LayoutParams.MATCH_PARENT
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
            R.id.view_mask -> {
                dismiss()
            }

            R.id.rtv_ok -> {
                if (selectedStreet != null) {
                    callback.selectStreet(selectedStreet!!)
                }
                dismiss()
            }
        }
    }

    interface StreetSelectCallBack {
        fun selectStreet(street: Street)
    }


}