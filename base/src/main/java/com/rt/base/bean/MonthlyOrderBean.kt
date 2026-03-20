package com.rt.base.bean

data class MonthlyOrderBean(
    var orderNo: String = "",
    var applyName: String = "",
    var carLicense: String = "",
    var carColor: String = "",
    var startTime: String = "",
    var endTime: String = "",
    var applyTime: String = "",
    var amount: String = "",
    var status: String = "",
    var streetName:String = "",
    var streetNo:String = ""
)

data class MonthlyOrderResultBean(
    var result: List<MonthlyOrderBean>
)