package com.rt.ipms_mg.ui.activity.monthlypay

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.rt.base.BaseApplication
import com.rt.base.arouter.ARouterMap
import com.rt.base.bean.MonthlyOrderBean
import com.rt.base.ds.PreferencesDataStore
import com.rt.base.ds.PreferencesKeys
import com.rt.base.ext.gone
import com.rt.base.ext.show
import com.rt.base.ext.startArouter
import com.rt.base.util.ToastUtil
import com.rt.base.viewbase.VbBaseActivity
import com.rt.common.util.GlideUtils
import com.rt.common.view.keyboard.KeyboardUtil
import com.rt.common.view.keyboard.MyOnTouchListener
import com.rt.common.view.keyboard.MyTextWatcher
import com.rt.ipms_mg.R
import com.rt.ipms_mg.adapter.MonthlyPrepaymentAdapter
import com.rt.ipms_mg.databinding.ActivityMonthlyPrepaymentListBinding
import com.rt.ipms_mg.mvvm.viewmodel.MonthlyPrepayMentViewModel
import kotlinx.coroutines.runBlocking

@Route(path = ARouterMap.MONTHLY_PREPAYMENT_LIST)
class MonthlyPrepaymentListActivity : VbBaseActivity<MonthlyPrepayMentViewModel, ActivityMonthlyPrepaymentListBinding>(), OnClickListener {
    private lateinit var keyboardUtil: KeyboardUtil
    var monthlyPrepaymentList: MutableList<MonthlyOrderBean> = ArrayList()
    var monthlyPrepaymentAdapter: MonthlyPrepaymentAdapter? = null
    var pageIndex = 1
    var pageSize = 10
    var loginName = ""

    override fun initView() {
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        GlideUtils.instance?.loadImage(binding.layoutToolbar.ivBack, com.rt.common.R.mipmap.ic_back_white)
        binding.layoutToolbar.tvTitle.text = "包月预付列表"
        binding.layoutToolbar.tvTitle.setTextColor(ContextCompat.getColor(BaseApplication.instance(), com.rt.base.R.color.white))

        binding.rvMonthlyPrepayment.setHasFixedSize(true)
        binding.rvMonthlyPrepayment.layoutManager = LinearLayoutManager(this)
        monthlyPrepaymentAdapter = MonthlyPrepaymentAdapter(monthlyPrepaymentList) { orderBean ->
            startArouter(ARouterMap.MONTHLY_PREPAYMENT_DETAIL, Bundle().apply {
                putString(ARouterMap.ORDER_ID, orderBean.orderNo)
            })
        }
        binding.rvMonthlyPrepayment.adapter = monthlyPrepaymentAdapter

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
        binding.tvSearch.setOnClickListener(this)
        binding.root.setOnClickListener(this)
        binding.layoutToolbar.toolbar.setOnClickListener(this)
        binding.ivCamera.setOnClickListener(this)
        binding.srlMonthlyPrepayment.setOnRefreshListener {
            pageIndex = 1
            binding.srlMonthlyPrepayment.finishRefresh(5000)
            monthlyPrepaymentList.clear()
            query()
        }
        binding.srlMonthlyPrepayment.setOnLoadMoreListener {
            pageIndex++
            query()
            binding.srlMonthlyPrepayment.finishLoadMore(5000)
        }
    }

    override fun initData() {
        runBlocking {
            loginName = PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.loginName)
            showProgressDialog(20000)
            // query()
            generateFakeData()
        }
    }

    private fun generateFakeData() {
        monthlyPrepaymentList.clear()
        for (i in 1..15) {
            // 不同状态循环：1-待审核，2-审核通过，3-审核拒绝，4-已完成
            val status = when (i % 4) {
                0 -> "1"  // 待审核
                1 -> "2"  // 审核通过
                2 -> "3"  // 审核拒绝
                else -> "4"  // 已完成
            }

            val orderBean = MonthlyOrderBean(
                amount = "${(100 + i * 10)}.00",
                carLicense = "京 A${String.format("%05d", i)}",
                startTime = "2026-03-${String.format("%02d", i % 28 + 1)} 08:00:00",
                endTime = "2026-${String.format("%02d", (i % 12) + 1)}-${String.format("%02d", i % 28 + 1)} 23:59:59",
                streetName = "xxxxxx${i}号路",
                orderNo = "NO${System.currentTimeMillis()}${i}",
                status = status
            )
            monthlyPrepaymentList.add(orderBean)
        }
        monthlyPrepaymentAdapter?.setList(monthlyPrepaymentList)
        dismissProgressDialog()
    }

    private fun query() {
        keyboardUtil.hideKeyboard()
        val searchContent = binding.etSearch.text.toString()
        if (searchContent.isNotEmpty() && (searchContent.length != 7 && searchContent.length != 8)) {
            dismissProgressDialog()
            ToastUtil.showMiddleToast("车牌长度只能是 7 位或 8 位")
            return
        }
        val param = HashMap<String, Any?>()
        val jsonobject = com.alibaba.fastjson.JSONObject()
        jsonobject["loginName"] = loginName
        jsonobject["carLicense"] = searchContent
        jsonobject["page"] = pageIndex
        jsonobject["size"] = pageSize
        param["attr"] = jsonobject
        mViewModel.monthlyPrepaymentList(param)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_back -> {
                onBackPressedSupport()
            }

            R.id.tv_search -> {
                pageIndex = 1
                showProgressDialog(20000)
                query()
            }

            R.id.toolbar,
            binding.root.id -> {
                keyboardUtil.hideKeyboard()
            }

            R.id.iv_camera -> {
                ARouter.getInstance().build(ARouterMap.SCAN_PLATE).navigation(this@MonthlyPrepaymentListActivity, 1)
            }
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            monthlyPrepaymentListLiveData.observe(this@MonthlyPrepaymentListActivity) {
                dismissProgressDialog()
                val tempList = it.result
                if (pageIndex == 1) {
                    if (tempList.isEmpty()) {
                        monthlyPrepaymentAdapter?.setNewInstance(null)
                        binding.rvMonthlyPrepayment.gone()
                        binding.layoutNoData.root.show()
                        binding.srlMonthlyPrepayment.finishRefresh()
                    } else {
                        monthlyPrepaymentList.clear()
                        monthlyPrepaymentList.addAll(tempList)
                        monthlyPrepaymentAdapter?.setList(monthlyPrepaymentList)
                        binding.srlMonthlyPrepayment.finishRefresh()
                        binding.rvMonthlyPrepayment.show()
                        binding.layoutNoData.root.gone()
                    }
                } else {
                    if (tempList.isEmpty()) {
                        pageIndex--
                        binding.srlMonthlyPrepayment.finishLoadMoreWithNoMoreData()
                    } else {
                        monthlyPrepaymentList.addAll(tempList)
                        monthlyPrepaymentAdapter?.setList(monthlyPrepaymentList)
                        binding.srlMonthlyPrepayment.finishLoadMore(300)
                    }
                }
            }
            errMsg.observe(this@MonthlyPrepaymentListActivity) {
                dismissProgressDialog()
                ToastUtil.showMiddleToast(it.msg)
            }
            mException.observe(this@MonthlyPrepaymentListActivity) {
                dismissProgressDialog()
            }
        }
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
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityMonthlyPrepaymentListBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun providerVMClass(): Class<MonthlyPrepayMentViewModel> {
        return MonthlyPrepayMentViewModel::class.java
    }

    override fun marginStatusBarView(): View? {
        return binding.layoutToolbar.ablToolbar
    }

    override fun isRegEventBus(): Boolean {
        return false
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