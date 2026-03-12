package com.rt.ipms_mg.mvvm.viewmodel

import com.rt.base.base.mvvm.BaseViewModel
import com.rt.ipms_mg.mvvm.repository.OrderRepository

class MonthlyPrepayMentViewModel: BaseViewModel() {

    val mOrderRepository by lazy {
        OrderRepository()
    }
}