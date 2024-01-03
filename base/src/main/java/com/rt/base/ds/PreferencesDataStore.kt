package com.rt.base.ds

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.properties.ReadOnlyProperty

/**
 * Created by hy on 2021/3/25.
 */
class PreferencesDataStore private constructor() : IDataStore {
    // 指定名字
    var context: Application? = null

    constructor(context: Application) : this() {
        this.context = context
    }

    companion object {
        private val PREFERENCES_NAME = "preferences_datastore_rt"
        val instance: PreferencesDataStore by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            PreferencesDataStore()
        }
        val corruptionHandler = ReplaceFileCorruptionHandler<Preferences> {
            mutablePreferencesOf(

            )
        }
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = PREFERENCES_NAME,
            corruptionHandler
        )

    }

    // 创建dataStore
//    val Context.ds by preferencesDataStore(
//        name = PREFERENCES_NAME,
//        produceMigrations = { context ->
//            listOf(SharedPreferencesMigration(context, PREFERENCES_NAME))
//        }
//    )

    override suspend fun putBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        context?.dataStore?.edit { it[key] = value }
    }

    override suspend fun getBoolean(key: Preferences.Key<Boolean>): Boolean {
        return context?.dataStore?.data?.map { it[key] ?: false }!!.first()
    }

    override suspend fun putInt(key: Preferences.Key<Int>, value: Int) {
        context?.dataStore?.edit { it[key] = value }
    }

    override suspend fun getInt(key: Preferences.Key<Int>): Int {
        return context?.dataStore?.data?.map { it[key] ?: 0 }!!.first()
    }

    override suspend fun putLong(key: Preferences.Key<Long>, value: Long) {
        context?.dataStore?.edit { it[key] = value }
    }

    override suspend fun getLong(key: Preferences.Key<Long>): Long {
        return context?.dataStore?.data?.map { it[key] ?: 0L }!!.first()
    }

    override suspend fun putDouble(key: Preferences.Key<Double>, value: Double) {
        context?.dataStore?.edit { it[key] = value }
    }

    override suspend fun getDouble(key: Preferences.Key<Double>): Double {
        return context?.dataStore?.data?.map { it[key] ?: 0.0 }!!.first()
    }

    override suspend fun putFloat(key: Preferences.Key<Float>, value: Float) {
        context?.dataStore?.edit { it[key] = value }
    }

    override suspend fun getFloat(key: Preferences.Key<Float>): Float {
        return context?.dataStore?.data?.map { it[key] ?: 0F }!!.first()
    }

    override suspend fun putString(key: Preferences.Key<String>, value: String) {
        context?.dataStore?.edit { it[key] = value }
    }

    override suspend fun getString(key: Preferences.Key<String>): String {
        return context?.dataStore?.data?.map { it[key] ?: "" }!!.first()
    }

}

// 而外一个存储 DataStore的Key的类
object PreferencesKeys {
    //    val WALLET_SIGN = booleanPreferencesKey("wallet_sign")
    val simId by stringPreferencesKey()

    val phone by stringPreferencesKey()

    val name by stringPreferencesKey()

    val loginName by stringPreferencesKey()

    val lastCheckUpdateTime by longPreferencesKey()

    fun booleanPreferencesKey() =
        ReadOnlyProperty<Any, Preferences.Key<Boolean>> { _, property -> booleanPreferencesKey(property.name) }

    fun stringPreferencesKey() =
        ReadOnlyProperty<Any, Preferences.Key<String>> { _, property -> stringPreferencesKey(property.name) }

    fun intPreferencesKey() =
        ReadOnlyProperty<Any, Preferences.Key<Int>> { _, property -> intPreferencesKey(property.name) }

    fun longPreferencesKey() =
        ReadOnlyProperty<Any, Preferences.Key<Long>> { _, property -> longPreferencesKey(property.name) }

    fun floatPreferencesKey() =
        ReadOnlyProperty<Any, Preferences.Key<Float>> { _, property -> floatPreferencesKey(property.name) }

    fun doublePreferencesKey() =
        ReadOnlyProperty<Any, Preferences.Key<Double>> { _, property -> doublePreferencesKey(property.name) }

    fun stringSetPreferencesKey() =
        ReadOnlyProperty<Any, Preferences.Key<Set<String>>> { _, property -> stringSetPreferencesKey(property.name) }

}

// 还有一个工厂类，去生成PreferencesDataStore的实例
//object StoreFactory {
//    @JvmStatic
//    fun providePreferencesDataStore(context: Application): IDataStore {
//        return PreferencesDataStore(context)
//    }
//}

