package com.rt.ipms_gm.ui.activity.parking

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.OnClickListener
import android.widget.PopupWindow
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.fastjson.JSONObject
import com.rt.base.BaseApplication
import com.rt.base.arouter.ARouterMap
import com.rt.base.bean.ParkingLotBean
import com.rt.base.bean.Street
import com.rt.base.ext.startArouter
import com.rt.base.util.ToastUtil
import com.rt.base.viewbase.VbBaseActivity
import com.rt.common.realm.RealmUtil
import com.rt.common.util.GlideUtils
import com.rt.ipms_gm.R
import com.rt.ipms_gm.adapter.ParkingLotAdapter
import com.rt.ipms_gm.databinding.ActivityParkingLotBinding
import com.rt.ipms_gm.mvvm.viewmodel.ParkingLotViewModel
import com.rt.ipms_gm.pop.StreetPop
import com.rt.common.event.EndOrderEvent
import com.rt.common.event.RefreshParkingLotEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ParkingLotActivity : VbBaseActivity<ParkingLotViewModel, ActivityParkingLotBinding>(), OnClickListener {
    var parkingLotAdapter: ParkingLotAdapter? = null
    var parkingLotList: MutableList<ParkingLotBean> = ArrayList()
    var currentStreet: Street? = null
    var count = 0
    var handler = Handler(Looper.getMainLooper())

    var streetPop: StreetPop? = null
    var streetList: MutableList<Street> = ArrayList()

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(refreshParkingLotEvent: RefreshParkingLotEvent) {
        getParkingLotList()
    }

    override fun initView() {
        GlideUtils.instance?.loadImage(binding.ivBack, com.rt.common.R.mipmap.ic_back_white)

        binding.rvParkingLot.setHasFixedSize(true)
        binding.rvParkingLot.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        parkingLotAdapter = ParkingLotAdapter(parkingLotList, this)
        binding.rvParkingLot.adapter = parkingLotAdapter
    }

    override fun initListener() {
        binding.flBack.setOnClickListener(this)
    }

    override fun initData() {
        streetList = RealmUtil.instance?.findCheckedStreetList() as MutableList<Street>
        currentStreet = RealmUtil.instance?.findCurrentStreet()
        if (currentStreet!!.streetName.indexOf("(") < 0) {
            binding.tvTitle.text = currentStreet!!.streetNo + currentStreet!!.streetName
        } else {
            binding.tvTitle.text =
                currentStreet!!.streetNo + currentStreet!!.streetName.substring(0, currentStreet!!.streetName.indexOf("("))
        }
        if (streetList.size == 1) {
            binding.tvTitle.setCompoundDrawables(
                null,
                null,
                null,
                null
            )
            binding.tvTitle.setOnClickListener(null)
        } else {
            binding.tvTitle.setOnClickListener(this)
        }
    }

    val runnable = object : Runnable {
        override fun run() {
            if (count < 600) {
                getParkingLotList()
                count++
                handler.postDelayed(this, 10000)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        handler.post(runnable)
    }

    fun getParkingLotList() {
        showProgressDialog(20000)
        val param = HashMap<String, Any>()
        val jsonobject = JSONObject()
        jsonobject["streetNo"] = currentStreet!!.streetNo
        param["attr"] = jsonobject
        mViewModel.getParkingLotList(param)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_back -> {
                onBackPressedSupport()
            }

            R.id.tv_title -> {
                currentStreet = RealmUtil.instance?.findCurrentStreet()
                streetPop = StreetPop(this@ParkingLotActivity, currentStreet, streetList, object : StreetPop.StreetSelectCallBack {
                    override fun selectStreet(street: Street) {
                        currentStreet = street
                        val old = RealmUtil.instance?.findCurrentStreet()
                        RealmUtil.instance?.updateCurrentStreet(street, old)
                        if (street.streetName.indexOf("(") < 0) {
                            binding.tvTitle.text = street.streetNo + street.streetName
                        } else {
                            binding.tvTitle.text =
                                street.streetNo + street.streetName.substring(0, street.streetName.indexOf("("))
                        }
                    }
                })
                streetPop?.showAsDropDown((v.parent) as Toolbar)
                val upDrawable = ContextCompat.getDrawable(BaseApplication.instance(), com.rt.common.R.mipmap.ic_arrow_up)
                upDrawable?.setBounds(0, 0, upDrawable.intrinsicWidth, upDrawable.intrinsicHeight)
                binding.tvTitle.setCompoundDrawables(
                    null,
                    null,
                    upDrawable,
                    null
                )
                streetPop?.setOnDismissListener(object : PopupWindow.OnDismissListener {
                    override fun onDismiss() {
                        val downDrawable =
                            ContextCompat.getDrawable(BaseApplication.instance(), com.rt.common.R.mipmap.ic_arrow_down)
                        downDrawable?.setBounds(0, 0, downDrawable.intrinsicWidth, downDrawable.intrinsicHeight)
                        binding.tvTitle.setCompoundDrawables(
                            null,
                            null,
                            downDrawable,
                            null
                        )
                    }
                })
            }

            R.id.rfl_parking -> {
                val parkingLotBean = v.tag as ParkingLotBean
                if (parkingLotBean.state == "01") {
                    startArouter(ARouterMap.ADMISSION_TAKE_PHOTO, data = Bundle().apply {
                        putString(ARouterMap.ADMISSION_TAKE_PHOTO_PARKING_NO, parkingLotBean.parkingNo)
                        putInt(ARouterMap.ADMISSION_TAKE_PHOTO_PARKING_AMOUNT, parkingLotList.size)
                    })
                } else {
                    startArouter(ARouterMap.PARKING_SPACE, data = Bundle().apply {
                        putString(ARouterMap.ORDER_NO, parkingLotBean.orderNo)
                        putString(ARouterMap.CAR_LICENSE, parkingLotBean.carLicense)
                        putString(ARouterMap.PARKING_NO, parkingLotBean.parkingNo)
                    })
                }
            }
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            parkingLotListLiveData.observe(this@ParkingLotActivity) {
                dismissProgressDialog()
                parkingLotList.clear()
                parkingLotList.addAll(it.result)
                parkingLotAdapter?.setList(parkingLotList)
            }
            errMsg.observe(this@ParkingLotActivity) {
                dismissProgressDialog()
                ToastUtil.showMiddleToast(it.msg)
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityParkingLotBinding.inflate(layoutInflater)
    }

    override fun onReloadData() {
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View {
        return binding.ablToolbar
    }

    override fun providerVMClass(): Class<ParkingLotViewModel> {
        return ParkingLotViewModel::class.java
    }

    override fun onDestroy() {
        super.onDestroy()
        if (handler != null) {
            handler.removeCallbacks(runnable)
        }
    }
}