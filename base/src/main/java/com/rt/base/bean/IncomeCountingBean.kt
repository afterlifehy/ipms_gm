package com.rt.base.bean

data class IncomeCountingBean(
    var loginName: String = "",
    var range: String = "",
    var list1: List<TodayIncomeBean>,
    var list2: List<RangeIncomeBean>
)

data class TodayIncomeBean(
    var onlineMoney: String = "",
    var orderCount: Int = 0,
    var oweCount: Int = 0,
    var oweMoney: String = "",
    var partPayCount: Int = 0,
    var passMoney: String = "",
    var payMoney: String = "",
    var refusePayCount: Int = 0
)

data class RangeIncomeBean(
    var orderCount: Int = 0,
    var payMoney: String = ""
)