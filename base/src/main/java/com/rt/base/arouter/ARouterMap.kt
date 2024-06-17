package com.rt.base.arouter

/**
 * Created by huy  on 2022/8/4.
 */
object ARouterMap {

    const val MAIN = "/ipms_mg_app/main"

    const val LOGIN = "/ipms_mg_app/login"

    const val LOGIN_INFO = "loginInfo"

    const val PARKING_SPACE = "/ipms_mg_app/parkingSpace"
    const val ORDER_NO = "orderNo"
    const val CAR_LICENSE = "carLicense"

    const val PREVIEW_IMAGE = "/ipms_mg_app/previewImg"
    const val IMG_INDEX = "imgIndex"
    const val IMG_LIST = "imgList"

    const val PIC = "/ipms_mg_app/pic"
    const val PIC_ORDER_NO = "orderNo"

    const val PREPAID = "/ipms_mg_app/prepaid"
    const val PREPAID_MIN_AMOUNT = "minAmount"
    const val PREPAID_CARLICENSE = "carLicense"
    const val PREPAID_PARKING_NO = "parkingNo"
    const val PREPAID_ORDER_NO = "orderNo"

    const val ORDER_INFO = "/ipms_mg_app/orderInfo"
    const val ORDER_INFO_ORDER_NO = "orderNo"

    const val ABNORMAL_REPORT = "/ipms_mg_app/abnormalReport"
    const val ABNORMAL_PARKING_NO = "parkingNo"
    const val ABNORMAL_CARLICENSE = "carLicense"

    const val ABNORMAL_HELP = "/ipms_mg_app/abnormalHelp"

    const val SCAN_PLATE = "/ipms_mg_app/scanPlate"

    const val ADMISSION_TAKE_PHOTO = "/ipms_mg_app/admissionTakePhoto"
    const val ADMISSION_TAKE_PHOTO_PARKING_NO = "parkingNo"
    const val ADMISSION_TAKE_PHOTO_PARKING_AMOUNT = "parkingAmount"

    const val DATA_PRINT = "/ipms_mg_app/dataPrint"
    const val DATA_PRINT_LOGIN_NAME = "loginName"

    const val MINE = "/ipms_mg_app/mine"
    const val MINE_BLUE_PRINT = "mineBluePrint"

    const val BASE_INFO = "/ipms_mg_app/baseInfo"

    const val FEE_RATE = "/ipms_mg_app/feeRate"

    const val INCOME_COUNTING = "/ipms_mg_app/incomeCounting"

    const val ORDER_MAIN = "/ipms_mg_app/orderMain"

    const val TRANSACTION_INQUIRY = "/ipms_mg_app/transactionInquiry"

    const val DEBT_COLLECTION = "/ipms_mg_app/debtCollection"
    const val DEBT_CAR_LICENSE = "carLicense"
    const val DEBT_CAR_TIME = "time"

    const val DEBT_ORDER_DETAIL = "/ipms_mg_app/debtOrderDetail"
    const val DEBT_ORDER = "debtOrder"

    const val ORDER_INQUIRY = "/ipms_mg_app/orderInquiry"

    const val ORDER_DETAIL = "/ipms_mg_app/orderDetail"
    const val ORDER = "order"

    const val TRANSACTION_RECORD = "/ipms_mg_app/transactionRecord"
    const val TRANSACTION_RECORD_ORDER_NO = "orderNo"

    class common {
        companion object {

        }
    }
}