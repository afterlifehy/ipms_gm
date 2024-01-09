package com.rt.ipms_mg.ui.activity.order

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.TimeUtils
import com.rt.base.BaseApplication
import com.rt.base.arouter.ARouterMap
import com.rt.base.bean.DebtCollectionBean
import com.rt.base.ds.PreferencesDataStore
import com.rt.base.ds.PreferencesKeys
import com.rt.base.ext.gone
import com.rt.base.ext.i18N
import com.rt.base.ext.i18n
import com.rt.base.ext.show
import com.rt.base.ext.startArouter
import com.rt.base.util.ToastUtil
import com.rt.base.viewbase.VbBaseActivity
import com.rt.common.event.RefreshDebtOrderListEvent
import com.rt.common.util.GlideUtils
import com.rt.common.view.keyboard.KeyboardUtil
import com.rt.common.view.keyboard.MyOnTouchListener
import com.rt.common.view.keyboard.MyTextWatcher
import com.rt.ipms_mg.R
import com.rt.ipms_mg.adapter.DebtCollectionAdapter
import com.rt.ipms_mg.databinding.ActivityDebtCollectionBinding
import com.rt.ipms_mg.mvvm.viewmodel.DebtCollectionViewModel
import com.rt.ipms_mg.pop.DatePop
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Route(path = ARouterMap.DEBT_COLLECTION)
class DebtCollectionActivity : VbBaseActivity<DebtCollectionViewModel, ActivityDebtCollectionBinding>(), OnClickListener {
    private lateinit var keyboardUtil: KeyboardUtil
    var debtCollectionAdapter: DebtCollectionAdapter? = null
    var debtCollectionList: MutableList<DebtCollectionBean> = ArrayList()
    var datePop: DatePop? = null
    var startDate = TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd")
    var endDate = TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd")
    var carLicense = ""
    var simId = ""

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(refreshDebtOrderListEvent: RefreshDebtOrderListEvent) {
        if (carLicense.isEmpty()) {
            return
        }
        query()
    }

    override fun initView() {
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        GlideUtils.instance?.loadImage(binding.layoutToolbar.ivBack, com.rt.common.R.mipmap.ic_back_white)
        binding.layoutToolbar.tvTitle.text = i18N(com.rt.base.R.string.欠费追缴)
        binding.layoutToolbar.tvTitle.setTextColor(ContextCompat.getColor(BaseApplication.instance(), com.rt.base.R.color.white))
        GlideUtils.instance?.loadImage(binding.layoutToolbar.ivRight, com.rt.common.R.mipmap.ic_calendar)
        binding.layoutToolbar.ivRight.show()
        GlideUtils.instance?.loadImage(binding.layoutNoData.ivNoData, com.rt.common.R.mipmap.ic_no_data_2)
        binding.layoutNoData.tvNoDataTitle.text = i18N(com.rt.base.R.string.通过车牌号未查询到欠费订单)

        if (intent.getStringExtra(ARouterMap.DEBT_CAR_LICENSE) != null) {
            carLicense = intent.getStringExtra(ARouterMap.DEBT_CAR_LICENSE).toString()
            binding.etSearch.setText(carLicense)
            binding.etSearch.setSelection(carLicense.length)
        }
        binding.rvDebt.setHasFixedSize(true)
        binding.rvDebt.layoutManager = LinearLayoutManager(this)
        debtCollectionAdapter = DebtCollectionAdapter(debtCollectionList, this)
        binding.rvDebt.adapter = debtCollectionAdapter

        initKeyboard()
    }

    private fun initKeyboard() {
        keyboardUtil = KeyboardUtil(binding.kvKeyBoard) {
            binding.etSearch.requestFocus()
            keyboardUtil.changeKeyboard(true)
            keyboardUtil.setEditText(binding.etSearch)
        }

        binding.etSearch.addTextChangedListener(MyTextWatcher(null, null, true, keyboardUtil))

        binding.etSearch.setOnTouchListener(MyOnTouchListener(true, binding.etSearch, keyboardUtil))

        binding.root.setOnClickListener {
            keyboardUtil.hideKeyboard()
        }
    }

    override fun initListener() {
        binding.layoutToolbar.flBack.setOnClickListener(this)
        binding.layoutToolbar.ivRight.setOnClickListener(this)
        binding.ivCamera.setOnClickListener(this)
        binding.tvSearch.setOnClickListener(this)
        binding.root.setOnClickListener(this)
        binding.layoutToolbar.toolbar.setOnClickListener(this)
    }

    override fun initData() {
        if (carLicense.isEmpty()) {
            return
        }
        query()
    }

    @SuppressLint("CheckResult")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_back -> {
                onBackPressedSupport()
            }

            R.id.iv_right -> {
                datePop = DatePop(BaseApplication.instance(), startDate, endDate, 0, object : DatePop.DateCallBack {
                    override fun selectDate(startTime: String, endTime: String) {
                        startDate = startTime
                        endDate = endTime
                        carLicense = binding.etSearch.text.toString()
                        if (carLicense.isEmpty()) {
                            ToastUtil.showMiddleToast(i18n(com.rt.base.R.string.请输入车牌号))
                            return
                        }
                        if (carLicense.length != 7 && carLicense.length != 8) {
                            ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.车牌长度只能是7位或8位))
                            return
                        }
                        showProgressDialog(20000)
                        query()
                    }

                })
                datePop?.showAsDropDown((v.parent) as Toolbar)
            }

            R.id.iv_camera -> {
                ARouter.getInstance().build(ARouterMap.SCAN_PLATE).navigation(this@DebtCollectionActivity, 1)
            }

            R.id.tv_search -> {
                carLicense = binding.etSearch.text.toString()
                if (carLicense.isEmpty()) {
                    ToastUtil.showMiddleToast(i18n(com.rt.base.R.string.请输入车牌号))
                    return
                }
                if (carLicense.length != 7 && carLicense.length != 8) {
                    ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.车牌长度只能是7位或8位))
                    return
                }
                query()
            }

            R.id.toolbar,
            binding.root.id -> {
                keyboardUtil.hideKeyboard()
            }

            R.id.rrl_debtCollection -> {
                val debtCollectionBean = v.tag as DebtCollectionBean
                startArouter(ARouterMap.DEBT_ORDER_DETAIL, data = Bundle().apply {
                    putParcelable(ARouterMap.DEBT_ORDER, debtCollectionBean)
                })
            }
        }
    }

    fun query() {
        keyboardUtil.hideKeyboard()
        showProgressDialog(20000)
        runBlocking {
            simId = PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.simId)
            val param = HashMap<String, Any>()
            val jsonobject = JSONObject()
            jsonobject["simId"] = simId
            jsonobject["plateId"] = carLicense
            jsonobject["startDate"] = startDate
            jsonobject["endDate"] = endDate
            param["attr"] = jsonobject
            mViewModel.debtInquiry(param)
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            debtInquiryLiveData.observe(this@DebtCollectionActivity) {
                dismissProgressDialog()
                debtCollectionList.clear()
                debtCollectionList.addAll(it.result)
                if (debtCollectionList.size > 0) {
                    binding.rvDebt.show()
                    binding.layoutNoData.root.gone()
                    debtCollectionAdapter?.setList(debtCollectionList)
                } else {
                    binding.rvDebt.gone()
                    binding.layoutNoData.root.show()
                }
            }
            errMsg.observe(this@DebtCollectionActivity) {
                dismissProgressDialog()
                ToastUtil.showMiddleToast(it.msg)
            }
        }
    }

    override fun providerVMClass(): Class<DebtCollectionViewModel> {
        return DebtCollectionViewModel::class.java
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                val plate = data?.getStringExtra("plate")
                if (!plate.isNullOrEmpty()) {
                    val plateId = if (plate.contains("新能源")) {
                        plate.substring(plate.length - 8, plate.length)
                    } else {
                        plate.substring(plate.length.minus(7) ?: 0, plate.length)
                    }
                    binding.etSearch.setText(plateId)
                    binding.etSearch.setSelection(plateId.length)
                }
            } else if (requestCode == 2) {
//                val plate = data?.getStringExtra("plate")
//                if (!plate.isNullOrEmpty()) {
//                    val plateId = if (plate.contains("新能源")) {
//                        plate.substring(plate.length - 8, plate.length)
//                    } else {
//                        plate.substring(plate.length.minus(7) ?: 0, plate.length)
//                    }
//                    collectionDialog?.setPlate(plateId)
//                    if (plate.startsWith("蓝")) {
//                        collectionDialog?.collectionPlateColorAdapter?.updateColor(Constant.BLUE, 0)
//                    } else if (plate.startsWith("绿")) {
//                        collectionDialog?.collectionPlateColorAdapter?.updateColor(Constant.GREEN, 1)
//                    } else if (plate.startsWith("黄")) {
//                        collectionDialog?.collectionPlateColorAdapter?.updateColor(Constant.YELLOW, 2)
//                    } else if (plate.startsWith("黄绿")) {
//                        collectionDialog?.collectionPlateColorAdapter?.updateColor(Constant.YELLOW_GREEN, 3)
//                    } else if (plate.startsWith("白")) {
//                        collectionDialog?.collectionPlateColorAdapter?.updateColor(Constant.WHITE, 4)
//                    } else if (plate.startsWith("黑")) {
//                        collectionDialog?.collectionPlateColorAdapter?.updateColor(Constant.BLACK, 5)
//                    } else {
//                        collectionDialog?.collectionPlateColorAdapter?.updateColor(Constant.OTHERS, 6)
//                    }
//                }
            }

        }
    }

    override fun isRegEventBus(): Boolean {
        return true
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityDebtCollectionBinding.inflate(layoutInflater)
    }

    override fun onReloadData() {
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View {
        return binding.layoutToolbar.ablToolbar
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyboardUtil.isShow()) {
                keyboardUtil.hideKeyboard()
            } else {
                return super.onKeyDown(keyCode, event)
            }
        }
        return false
    }
}