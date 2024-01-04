package com.rt.ipms_mg.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rt.base.base.mvvm.BaseViewModel
import com.rt.base.bean.UpdateBean
import com.rt.ipms_mg.mvvm.repository.MineRepository

class MainViewModel : BaseViewModel() {
    val mMineRepository by lazy {
        MineRepository()
    }

    val checkUpdateLiveDate = MutableLiveData<UpdateBean>()

    fun checkUpdate(param: Map<String, Any?>) {
        checkUpdateLiveDate.value = UpdateBean("0", "0", "21321312", "1.0.1")
//        launch {
//            val response = withContext(Dispatchers.IO) {
//                mMineRepository.checkUpdate(param)
//            }
//            executeResponse(response, {
//                checkUpdateLiveDate.value = response.attr
//            }, {
//                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
//            })
//        }
    }
}