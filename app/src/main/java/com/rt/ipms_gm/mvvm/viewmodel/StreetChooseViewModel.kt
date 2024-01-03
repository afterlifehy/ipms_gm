package com.rt.ipms_gm.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rt.base.base.mvvm.BaseViewModel
import com.rt.base.base.mvvm.ErrorMessage
import com.rt.ipms_gm.mvvm.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StreetChooseViewModel : BaseViewModel() {
    val mLoginRepository by lazy {
        LoginRepository()
    }

    val checkOnWorkLiveData = MutableLiveData<Any>()

    fun checkOnWork(param: Map<String, Any?>) {
        launch {
            val response = withContext(Dispatchers.IO) {
                mLoginRepository.checkOnWork(param)
            }
            executeResponse(response, {
                checkOnWorkLiveData.value = response.attr
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
            })
        }
    }
}