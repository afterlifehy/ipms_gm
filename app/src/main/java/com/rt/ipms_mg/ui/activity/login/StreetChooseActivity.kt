package com.rt.ipms_mg.ui.activity.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.view.View
import android.view.View.OnClickListener
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.TimeUtils
import com.tbruyelle.rxpermissions3.RxPermissions
import com.rt.base.BaseApplication
import com.rt.base.arouter.ARouterMap
import com.rt.base.bean.LoginBean
import com.rt.base.bean.Street
import com.rt.base.bean.WorkingHoursBean
import com.rt.base.ds.PreferencesDataStore
import com.rt.base.ds.PreferencesKeys
import com.rt.base.ext.i18N
import com.rt.base.util.ToastUtil
import com.rt.base.viewbase.VbBaseActivity
import com.rt.common.realm.RealmUtil
import com.rt.ipms_mg.R
import com.rt.ipms_mg.adapter.StreetChoosedAdapter
import com.rt.ipms_mg.databinding.ActivityStreetChooseBinding
import com.rt.ipms_mg.dialog.StreetChooseListDialog
import com.rt.ipms_mg.mvvm.viewmodel.StreetChooseViewModel
import com.rt.common.util.AppUtil
import kotlinx.coroutines.runBlocking

class StreetChooseActivity : VbBaseActivity<StreetChooseViewModel, ActivityStreetChooseBinding>(),
    OnClickListener {
    var streetList: MutableList<Street> = ArrayList()
    var streetChooseListDialog: StreetChooseListDialog? = null
    var streetChoosedAdapter: StreetChoosedAdapter? = null
    var streetChoosedList: MutableList<Street> = ArrayList()
    var loginInfo: LoginBean? = null

    var locationManager: LocationManager? = null
    var lat = 0.00
    var lon = 0.00
    var locationEnable = 0

    @SuppressLint("CheckResult", "MissingPermission")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun initView() {
        binding.layoutToolbar.tvTitle.text = i18N(com.rt.base.R.string.路段选择)

        loginInfo = intent.getParcelableExtra(ARouterMap.LOGIN_INFO) as? LoginBean

        binding.rvStreet.setHasFixedSize(true)
        binding.rvStreet.layoutManager = LinearLayoutManager(this)
        streetChoosedAdapter = StreetChoosedAdapter(streetChoosedList, this)
        binding.rvStreet.adapter = streetChoosedAdapter

        var rxPermissions = RxPermissions(this@StreetChooseActivity)
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe {
            if (it) {
                locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val provider = LocationManager.NETWORK_PROVIDER
                locationManager?.requestLocationUpdates(provider, 1000, 1f, @SuppressLint("MissingPermission")
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        lat = location.latitude
                        lon = location.longitude
                        locationEnable = 1
                    }

                    override fun onProviderDisabled(provider: String) {
                        locationEnable = -1
                        ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.请打开位置信息))
                    }

                    override fun onProviderEnabled(provider: String) {
                        locationEnable = 1
                    }
                })
            }
        }
    }

    override fun initListener() {
        binding.layoutToolbar.flBack.setOnClickListener(this)
        binding.rflAddStreet.setOnClickListener(this)
        binding.rtvEnterWorkBench.setOnClickListener(this)
    }

    override fun initData() {
        streetList.clear()
        streetList = loginInfo?.result as MutableList<Street>
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_back -> {
                onBackPressedSupport()
            }

            R.id.rfl_addStreet -> {
                streetChooseListDialog =
                    StreetChooseListDialog(streetList, streetChoosedList, object : StreetChooseListDialog.StreetChooseCallBack {
                        override fun chooseStreets() {
                            streetChoosedAdapter?.setList(streetChoosedList.distinct())
                        }

                    })
                streetChooseListDialog?.show()
            }

            R.id.rtv_enterWorkBench -> {
                if (streetChoosedList.isNotEmpty()) {
                    showProgressDialog(20000)
                    val param = HashMap<String, Any>()
                    val jsonobject = JSONObject()
                    jsonobject["loginName"] = loginInfo?.loginName
                    jsonobject["streetNos"] = streetChoosedList.joinToString(separator = ",") { it.streetNo }
                    jsonobject["longitude"] = lon
                    jsonobject["latitude"] = lat
                    param["attr"] = jsonobject
                    mViewModel.checkOnWork(param)
                } else {
                    ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.请添加路段))
                }
            }

            R.id.rfl_delete -> {
                if(!AppUtil.isFastClick(500)){
                    val item = v.tag as Street
                    val position = streetChoosedList.indexOf(item)
                    streetChoosedList.remove(item)
                    streetChoosedAdapter?.removeAt(position)
                }
            }
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            checkOnWorkLiveData.observe(this@StreetChooseActivity) {
                dismissProgressDialog()
                for (i in streetChoosedList) {
                    RealmUtil.instance?.updateStreetChoosed(i)
                }
                runBlocking {
                    PreferencesDataStore(BaseApplication.instance()).putString(PreferencesKeys.simId, loginInfo!!.simId)
                    PreferencesDataStore(BaseApplication.instance()).putString(PreferencesKeys.phone, loginInfo!!.phone)
                    PreferencesDataStore(BaseApplication.instance()).putString(PreferencesKeys.name, loginInfo!!.name)
                    PreferencesDataStore(BaseApplication.instance()).putString(PreferencesKeys.loginName, loginInfo!!.loginName)
                }
                RealmUtil.instance?.deleteAllStreet()
                RealmUtil.instance?.addRealmAsyncList(streetChoosedList)
                RealmUtil.instance?.updateCurrentStreet(streetChoosedList[0], null)

                val workingHoursBean = RealmUtil.instance?.findCurrentWorkingHour(loginInfo!!.loginName)
                if (workingHoursBean != null) {
                    val lastDay = TimeUtils.millis2String(workingHoursBean.time, "yyyy-MM-dd")
                    val currentDay = TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd")
                    if (lastDay != currentDay) {
                        RealmUtil.instance?.addRealm(WorkingHoursBean(loginInfo!!.loginName, System.currentTimeMillis()))
                    }
                } else {
                    RealmUtil.instance?.addRealm(WorkingHoursBean(loginInfo!!.loginName, System.currentTimeMillis()))
                }
                ARouter.getInstance().build(ARouterMap.MAIN).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
            }
            errMsg.observe(this@StreetChooseActivity) {
                dismissProgressDialog()
                ToastUtil.showMiddleToast(it.msg)
            }
            mException.observe(this@StreetChooseActivity){
                dismissProgressDialog()
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityStreetChooseBinding.inflate(layoutInflater)
    }

    override fun onReloadData() {
    }

    override val isFullScreen: Boolean
        get() = true

    override fun providerVMClass(): Class<StreetChooseViewModel> {
        return StreetChooseViewModel::class.java
    }

    override fun marginStatusBarView(): View {
        return binding.layoutToolbar.ablToolbar
    }
}