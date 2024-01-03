package com.rt.base.bean

data class EndOrderInfoBean(
    var carLicense: String = "",
    var hasPayed: String = "",
    var havePayMoney: String = "",
    var orderMoney: String = "",
    var parkingHours: Int = 0,
    var realtimeMoney: String = ""
)