package com.rt.base.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class DebtCollectionResultBean(
    var result: List<DebtCollectionBean>
)

@Parcelize
data class DebtCollectionBean @JvmOverloads constructor(
    var oweMoney: Int,
    var paidMoney: Int,
    var carLicense: String? = "",
    var dueMoney: Int,
    var endTime: String = "",
    var orderNo: String = "",
    var parkingNo: String = "",
    var startTime: String = "",
    var streetName: String = "",
    var streetNo: String = ""
) : Parcelable {
    init {
        if (carLicense == null) {
            carLicense = ""
        }
    }
}
