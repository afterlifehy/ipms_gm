package com.rt.ipms_gm.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rt.base.base.mvvm.BaseViewModel
import com.rt.base.base.mvvm.ErrorMessage
import com.rt.base.bean.PayQRBean
import com.rt.base.bean.PayResultBean
import com.rt.base.bean.PicInquiryBean
import com.rt.ipms_gm.mvvm.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DebtOrderDetailViewModel : BaseViewModel() {
    val mOrderRepository by lazy {
        OrderRepository()
    }

    val debtPayQrLiveData = MutableLiveData<PayQRBean>()
    val payResultInquiryLiveData = MutableLiveData<PayResultBean>()
    val picInquiryLiveData = MutableLiveData<PicInquiryBean>()

    fun picInquiry(param: Map<String, Any?>) {
        launch {
            val response = withContext(Dispatchers.IO) {
                mOrderRepository.picInquiry(param)
            }
            executeResponse(response, {
                picInquiryLiveData.value = response.attr
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
            })
        }
    }

    fun debtPayQr(param: Map<String, Any?>) {
        launch {
            val response = withContext(Dispatchers.IO) {
                mOrderRepository.debtPayQr(param)
            }
            executeResponse(response, {
                debtPayQrLiveData.value = response.attr
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
                traverseErrorMsg(ErrorMessage(msg = "", code = response.status))
            })
        }
    }
}