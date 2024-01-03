package com.rt.ipms_gm.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rt.base.base.mvvm.BaseViewModel
import com.rt.base.base.mvvm.ErrorMessage
import com.rt.base.bean.PayResultBean
import com.rt.base.bean.PayQRBean
import com.rt.ipms_gm.mvvm.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PrepaidViewModel: BaseViewModel() {
    val mOrderRepository by lazy {
        OrderRepository()
    }

    val prePayFeeInquiryLiveData = MutableLiveData<PayQRBean>()
    val payResultInquiryLiveData = MutableLiveData<PayResultBean>()

    fun prePayFeeInquiry(param: Map<String, Any?>) {
        launch {
            val response = withContext(Dispatchers.IO) {
                mOrderRepository.prePayFeeInquiry(param)
            }
            executeResponse(response, {
                prePayFeeInquiryLiveData.value = response.attr
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
            })
        }
    }

    fun payResultInquiry(param: Map<String, Any?>) {
        launch {
            val response = withContext(Dispatchers.IO) {
                mOrderRepository.payResultInquiry(param)
            }
            executeResponse(response, {
                payResultInquiryLiveData.value = response.attr
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
            })
        }
    }
}