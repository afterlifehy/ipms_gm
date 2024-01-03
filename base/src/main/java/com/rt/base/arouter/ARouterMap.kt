package com.rt.base.arouter

/**
 * Created by huy  on 2022/8/4.
 */
object ARouterMap {

    const val MAIN = "/inspector_app/main"

    const val LOGIN = "/inspector_app/login"

    const val LOGIN_INFO = "loginInfo"

    const val PARKING_SPACE = "/inspector_app/parkingSpace"
    const val ORDER_NO = "orderNo"
    const val CAR_LICENSE = "carLicense"
    const val PARKING_NO = "parkingNo"

    const val PREVIEW_IMAGE = "/inspector_app/previewImg"
    const val IMG_INDEX = "imgIndex"
    const val IMG_LIST = "imgList"

    const val PIC = "/inspector_app/pic"
    const val PIC_ORDER_NO = "orderNo"

    const val PREPAID = "/inspector_app/prepaid"
    const val PREPAID_MIN_AMOUNT = "minAmount"
    const val PREPAID_CARLICENSE = "carLicense"
    const val PREPAID_PARKING_NO = "parkingNo"
    const val PREPAID_ORDER_NO = "orderNo"

    const val ORDER_INFO = "/inspector_app/orderInfo"
    const val ORDER_INFO_ORDER_NO = "orderNo"

    const val ABNORMAL_REPORT = "/inspector_app/abnormalReport"
    const val ABNORMAL_PARKING_NO = "parkingNo"
    const val ABNORMAL_ORDER_NO = "orderNo"
    const val ABNORMAL_CARLICENSE = "carLicense"

    const val ABNORMAL_HELP = "/inspector_app/abnormalHelp"

    const val SCAN_PLATE = "/inspector_app/scanPlate"

    const val ADMISSION_TAKE_PHOTO = "/inspector_app/admissionTakePhoto"
    const val ADMISSION_TAKE_PHOTO_PARKING_NO = "parkingNo"
    const val ADMISSION_TAKE_PHOTO_PARKING_AMOUNT = "parkingAmount"

    const val DATA_PRINT = "/inspector_app/dataPrint"
    const val DATA_PRINT_LOGIN_NAME = "loginName"

    const val MINE = "/inspector_app/mine"
    const val MINE_BLUE_PRINT = "mineBluePrint"

    const val BASE_INFO = "/inspector_app/baseInfo"

    const val FEE_RATE = "/inspector_app/feeRate"

    const val INCOME_COUNTING = "/inspector_app/incomeCounting"

    const val ORDER_MAIN = "/inspector_app/orderMain"

    const val TRANSACTION_INQUIRY = "/inspector_app/transactionInquiry"

    const val DEBT_COLLECTION = "/inspector_app/debtCollection"
    const val DEBT_CAR_LICENSE = "carLicense"

    const val DEBT_ORDER_DETAIL = "/inspector_app/debtOrderDetail"
    const val DEBT_ORDER = "debtOrder"

    const val ORDER_INQUIRY = "/inspector_app/orderInquiry"

    const val ORDER_DETAIL = "/inspector_app/orderDetail"
    const val ORDER = "order"

    const val TRANSACTION_RECORD = "/inspector_app/transactionRecord"
    const val TRANSACTION_RECORD_ORDER_NO = "orderNo"

    class common {
        companion object {

        }
    }
}