package com.rt.ipms_mg.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rt.base.base.mvvm.BaseViewModel
import com.rt.ipms_mg.mvvm.repository.LogoutRepository

class LogoutViewModel : BaseViewModel() {
    val mLogoutRepository by lazy {
        LogoutRepository()
    }

    val logoutLiveData = MutableLiveData<Any>()

    fun logout(param: Map<String, Any?>) {
        logoutLiveData.value = true
//        launch {
//            val response = withContext(Dispatchers.IO) {
//                mLogoutRepository.logout(param)
//            }
//            executeResponse(response, {
//                logoutLiveData.value = response.attr
//            }, {
//                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
//            })
//        }
    }
}