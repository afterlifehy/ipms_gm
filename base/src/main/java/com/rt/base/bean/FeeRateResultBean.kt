package com.rt.base.bean

data class FeeRateResultBean(
    var result: List<FeeRateBean>
)

data class FeeRateBean(
    var blackEnd: String = "",
    var blackStart: String = "",
    var dateType: Int = 0,
    var period: String = "",
    var unitPrice: String = "",
    var whiteEnd: String = "",
    var whiteStart: String = "",
    var firstHourMoney: String = ""
)