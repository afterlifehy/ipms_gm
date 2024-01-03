package com.rt.base.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class DebtCollectionResultBean(
    var result: List<DebtCollectionBean>
)

@Parcelize
data class DebtCollectionBean @JvmOverloads constructor(
    var carLicense: String? = "",
    var companyName: String = "",
    var companyPhone: String = "",
    var districtId: String = "",
    var dueMoney: Int,
    var endTime: String = "",
    var orderNo: String = "",
    var orderType: Int,
    var oweMoney: Int,
    var oweOrderId: String = "",
    var paidMoney: Int,
    var parkingNo: String = "",
    var parkingTime: String = "",
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
