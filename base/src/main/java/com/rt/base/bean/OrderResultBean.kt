package com.rt.base.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class OrderResultBean(
    var result: List<OrderBean>
)

@Parcelize
data class OrderBean(
    var amount: String = "",
    var carLicense: String = "",
    var duration: String = "0",
    var endTime: String = "",
    var hasPayed: String = "",
    var orderNo: String = "",
    var parkingNo: String = "",
    var startTime: String = "",
    var streetName: String = "",
    var paidAmount: String = "",
    var isPrinted: String = "0"
) : Parcelable {
    init {
        if (duration.isNullOrEmpty()) {
            duration = "0"
        }
    }
}