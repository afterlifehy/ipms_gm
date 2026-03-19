package com.rt.ipms_mg.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rt.base.base.mvvm.BaseViewModel
import com.rt.base.base.mvvm.ErrorMessage
import com.rt.base.bean.OrderResultBean
import com.rt.ipms_mg.mvvm.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MonthlyPrepayMentViewModel: BaseViewModel() {

    val mOrderRepository by lazy {
        OrderRepository()
    }

    val monthlyPrepaymentListLiveData = MutableLiveData<OrderResultBean>()

    fun monthlyPrepaymentList(param: Map<String, Any?>) {
        launch {
            val response = withContext(Dispatchers.IO) {
                mOrderRepository.monthlyPrepaymentList(param)
            }
            executeResponse(response, {
                monthlyPrepaymentListLiveData.value = response.attr
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
            })
        }
    }
}