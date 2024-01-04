package com.rt.ipms_mg.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rt.base.BaseApplication
import com.rt.base.base.mvvm.BaseViewModel
import com.rt.base.ds.PreferencesDataStore
import com.rt.base.ds.PreferencesKeys
import com.rt.common.realm.RealmUtil
import kotlinx.coroutines.runBlocking

open class BaseInfoViewModel : BaseViewModel() {
    val baseInfoLiveData = MutableLiveData<MutableList<String>>()

    fun getBaseInfo() {
        val baseInfoList: MutableList<String> = ArrayList()
        val street = RealmUtil.instance?.findCurrentStreet()
        return runBlocking {
            val name = PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.name)
            val loginName = PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.loginName)
            val phone = PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.phone)

            baseInfoList.add(name)
            baseInfoList.add(loginName)
            baseInfoList.add(phone)
            baseInfoList.add(street!!.streetName)
            baseInfoLiveData.value = baseInfoList
        }
    }
}