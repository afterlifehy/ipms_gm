package com.rt.ipms_gm.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rt.base.base.mvvm.BaseViewModel
import com.rt.base.base.mvvm.ErrorMessage
import com.rt.ipms_gm.mvvm.repository.LogoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogoutViewModel : BaseViewModel() {
    val mLogoutRepository by lazy {
        LogoutRepository()
    }

    val logoutLiveData = MutableLiveData<Any>()

    fun logout(param: Map<String, Any?>) {
        launch {
            val response = withContext(Dispatchers.IO) {
                mLogoutRepository.logout(param)
            }
            executeResponse(response, {
                logoutLiveData.value = response.attr
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
            })
        }
    }
}