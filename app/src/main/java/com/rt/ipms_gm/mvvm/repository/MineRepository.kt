package com.rt.ipms_gm.mvvm.repository

import com.rt.base.base.mvvm.BaseRepository
import com.rt.base.bean.FeeRateResultBean
import com.rt.base.bean.HttpWrapper
import com.rt.base.bean.IncomeCountingBean
import com.rt.base.bean.UpdateBean

class MineRepository : BaseRepository() {
    /**
     * 版本更新查询
     */
    suspend fun checkUpdate(param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<UpdateBean> {
        return mServer.checkUpdate(param)
    }

    /**
     * 营收打印
     */
    suspend fun incomeCounting(param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<IncomeCountingBean> {
        return mServer.incomeCounting(param)
    }

    /**
     * 费率
     */
    suspend fun feeRate(param: @JvmSuppressWildcards Map<String, Any?>): HttpWrapper<FeeRateResultBean> {
        return mServer.feeRate(param)
    }
}