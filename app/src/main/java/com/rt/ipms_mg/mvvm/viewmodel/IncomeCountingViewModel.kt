package com.rt.ipms_mg.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rt.base.base.mvvm.BaseViewModel
import com.rt.base.base.mvvm.ErrorMessage
import com.rt.base.bean.IncomeCountingBean
import com.rt.ipms_mg.mvvm.repository.IncomeCountingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IncomeCountingViewModel: BaseViewModel() {

    val mIncomeCountingRepository by lazy {
        IncomeCountingRepository()
    }

    val incomeCountingLiveData = MutableLiveData<IncomeCountingBean>()

    fun incomeCounting(param: Map<String, Any?>) {
        launch {
            val response = withContext(Dispatchers.IO) {
                mIncomeCountingRepository.incomeCounting(param)
            }
            executeResponse(response, {
                incomeCountingLiveData.value = response.attr
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
            })
        }
    }
}