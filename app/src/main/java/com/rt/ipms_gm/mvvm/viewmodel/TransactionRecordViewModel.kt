package com.rt.ipms_gm.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rt.base.base.mvvm.BaseViewModel
import com.rt.base.base.mvvm.ErrorMessage
import com.rt.base.bean.TicketPrintResultBean
import com.rt.base.bean.TransactionBean
import com.rt.base.bean.TransactionResultBean
import com.rt.ipms_gm.mvvm.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransactionRecordViewModel : BaseViewModel() {

    val mOrderRepository by lazy {
        OrderRepository()
    }

    val inquiryTransactionByOrderNoLiveData = MutableLiveData<TicketPrintResultBean>()

    fun inquiryTransactionByOrderNo(param: Map<String, Any?>) {
        launch {
            val response = withContext(Dispatchers.IO) {
                mOrderRepository.inquiryTransactionByOrderNo(param)
            }
            executeResponse(response, {
                inquiryTransactionByOrderNoLiveData.value = response.attr
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
            })
        }
    }
}