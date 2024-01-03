package com.rt.ipms_gm.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rt.base.base.mvvm.BaseViewModel
import com.rt.base.base.mvvm.ErrorMessage
import com.rt.base.bean.ParkingSpaceBean
import com.rt.base.bean.TicketPrintBean
import com.rt.base.bean.TicketPrintResultBean
import com.rt.ipms_gm.mvvm.repository.ParkingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ParkingSpaceViewModel : BaseViewModel() {
    val mParkingRepository by lazy {
        ParkingRepository()
    }

    val parkingSpaceLiveData = MutableLiveData<ParkingSpaceBean>()
    val endOrderLiveData = MutableLiveData<Any>()
    val picUploadLiveData = MutableLiveData<Any>()
    val inquiryTransactionByOrderNoLiveData = MutableLiveData<TicketPrintResultBean>()

    fun parkingSpace(param: Map<String, Any?>) {
        launch {
            val response = withContext(Dispatchers.IO) {
                mParkingRepository.parkingSpace(param)
            }
            executeResponse(response, {
                parkingSpaceLiveData.value = response.attr
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
            })
        }
    }

    fun endOrder(param: Map<String, Any?>) {
        launch {
            val response = withContext(Dispatchers.IO) {
                mParkingRepository.endOrder(param)
            }
            executeResponse(response, {
                endOrderLiveData.value = response.attr
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
            })
        }
    }

    fun picUpload(param: Map<String, Any?>) {
        launch {
            val response = withContext(Dispatchers.IO) {
                mParkingRepository.picUpload(param)
            }
            executeResponse(response, {
                picUploadLiveData.value = response.attr
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
            })
        }
    }

    fun inquiryTransactionByOrderNo(param: Map<String, Any?>) {
        launch {
            val response = withContext(Dispatchers.IO) {
                mParkingRepository.inquiryTransactionByOrderNo(param)
            }
            executeResponse(response, {
                inquiryTransactionByOrderNoLiveData.value = response.attr
            }, {
                traverseErrorMsg(ErrorMessage(msg = response.msg, code = response.status))
            })
        }
    }
}