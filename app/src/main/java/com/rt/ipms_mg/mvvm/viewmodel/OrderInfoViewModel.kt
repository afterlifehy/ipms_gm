package com.rt.ipms_mg.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rt.base.base.mvvm.BaseViewModel
import com.rt.base.base.mvvm.ErrorMessage
import com.rt.base.bean.DebtUploadBean
import com.rt.base.bean.EndOrderInfoBean
import com.rt.base.bean.PayQRBean
import com.rt.base.bean.PayResultBean
import com.rt.ipms_mg.mvvm.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderInfoViewModel : BaseViewModel() {

    val mOrderRepository by lazy {
        OrderRepository()
    }

    val endOrderInfoLiveData = MutableLiveData<EndOrderInfoBean>()
    val endOrderQRLiveData = MutableLiveData<PayQRBean>()
    val debtUploadLiveData = MutableLiveData<DebtUploadBean>()

    fun endOrderInfo(param: Map<String, Any?>) {
        launch {

            val response = withContext(Dispatchers.IO) {
                mOrderRepository.endOrderInfo(param)
            }
            executeResponse(response, {
                endOrderInfoLiveData.value = response.attr
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
            })
        }
    }

    fun endOrderQR(param: Map<String, Any?>) {
        launch {
            val response = withContext(Dispatchers.IO) {
                mOrderRepository.endOrderQR(param)
            }
            executeResponse(response, {
                endOrderQRLiveData.value = response.attr
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
            })
        }
    }

}