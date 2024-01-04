package com.rt.ipms_gm.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rt.base.base.mvvm.BaseViewModel
import com.rt.base.base.mvvm.ErrorMessage
import com.rt.base.bean.OrderNoBean
import com.rt.ipms_gm.mvvm.repository.AbnormalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AbnormalReportViewModel: BaseViewModel() {
    val mAbnormalRepository by lazy {
        AbnormalRepository()
    }

    val abnormalReportLiveData = MutableLiveData<Any>()
    val picUploadLiveData = MutableLiveData<Any>()
    val inquiryOrderNoByParkingNoLiveData = MutableLiveData<OrderNoBean>()

    fun abnormalReport(param: Map<String, Any?>) {
        launch {
            val response = withContext(Dispatchers.IO) {
                mAbnormalRepository.abnormalReport(param)
            }
            executeResponse(response, {
                abnormalReportLiveData.value = response.attr
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
            })
        }
    }

    fun picUpload(param: Map<String, Any?>) {
        launch {
            val response = withContext(Dispatchers.IO) {
                mAbnormalRepository.picUpload(param)
            }
            executeResponse(response, {
                picUploadLiveData.value = response.attr
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
            })
        }
    }

    fun inquiryOrderNoByParkingNo(param: Map<String, Any?>) {
        launch {
            val response = withContext(Dispatchers.IO) {
                mAbnormalRepository.inquiryOrderNoByParkingNo(param)
            }
            executeResponse(response, {
                inquiryOrderNoByParkingNoLiveData.value = response.attr
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
            })
        }
    }
}