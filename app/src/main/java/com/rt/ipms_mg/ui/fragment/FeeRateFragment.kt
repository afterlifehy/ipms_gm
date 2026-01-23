package com.rt.ipms_mg.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.alibaba.fastjson.JSONObject
import com.rt.base.BaseApplication
import com.rt.base.bean.FeeRateBean
import com.rt.base.util.ToastUtil
import com.rt.base.viewbase.VbBaseFragment
import com.rt.ipms_mg.adapter.FeeRateAdapter
import com.rt.ipms_mg.databinding.FragmentFeeRateBinding
import com.rt.ipms_mg.mvvm.viewmodel.FeeRateFragmentViewModel

class FeeRateFragment : VbBaseFragment<FeeRateFragmentViewModel, FragmentFeeRateBinding>() {
    var feeRateAdapter: FeeRateAdapter? = null
    var feeRateList: MutableList<FeeRateBean> = ArrayList()
    var streetNo = ""
    override fun initView() {
        streetNo = arguments?.getString("streetNo").toString()

        binding.rvFeeRate.setHasFixedSize(true)
        binding.rvFeeRate.layoutManager = LinearLayoutManager(BaseApplication.instance())
        feeRateAdapter = FeeRateAdapter(feeRateList)
        binding.rvFeeRate.adapter = feeRateAdapter
    }

    override fun initData() {
        showProgressDialog()
        val param = HashMap<String, Any>()
        val jsonobject = JSONObject()
        jsonobject["streetNo"] = streetNo
        param["attr"] = jsonobject
        mViewModel.feeRate(param)
    }

    override fun initListener() {
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            feeRateLiveData.observe(this@FeeRateFragment) {
                dismissProgressDialog()
                feeRateList = it.result as MutableList<FeeRateBean>
                feeRateAdapter?.setList(feeRateList)
            }
            errMsg.observe(this@FeeRateFragment) {
                dismissProgressDialog()
                ToastUtil.showMiddleToast(it.msg)
            }
        }
    }

    override fun providerVMClass(): Class<FeeRateFragmentViewModel> {
        return FeeRateFragmentViewModel::class.java
    }

    override fun getVbBindingView(): ViewBinding {
        return FragmentFeeRateBinding.inflate(layoutInflater)
    }
}