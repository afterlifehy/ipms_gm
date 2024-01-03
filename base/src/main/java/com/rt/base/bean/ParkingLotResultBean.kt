package com.rt.base.bean

data class ParkingLotResultBean(
    var result: List<ParkingLotBean>
)

data class ParkingLotBean(
    var carColor: String,
    var carLicense: String,
    var cleared: String,
    var orderNo: String,
    var parkingNo: String,
    var state: String,
    var deadLine:Long
)