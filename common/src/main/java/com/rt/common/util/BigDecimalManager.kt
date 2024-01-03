package com.rt.common.util

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Created by huy  on 2022/11/4.
 */
object BigDecimalManager {
    /**
     * double类型的加法运算（不需要舍入）
     *
     * @param m1
     * @param m2
     * @return 不加doubleValue()则, 返回BigDecimal对象
     */
    fun additionDouble(m1: Double, m2: Double): Double {
        val p1 = BigDecimal(m1.toString())
        val p2 = BigDecimal(m2.toString())
        return p1.add(p2).toDouble()
    }

    /**
     * double类型的加法运算（不需要舍入）
     *
     * @param m1
     * @param m2
     * @return 不加doubleValue()则, 返回BigDecimal对象
     */
    fun additionDoubleToString(m1: Double, m2: Double): String {
        val p1 = BigDecimal(AppUtil.replaceComma(m1.toString()))
        val p2 = BigDecimal(AppUtil.replaceComma(m2.toString()))
        return p1.add(p2).stripTrailingZeros().toPlainString()
    }

    /**
     * double类型的加法运算（不需要舍入）
     *
     * @param m1
     * @param m2
     * @return 不加doubleValue()则, 返回BigDecimal对象
     */
    fun additionDoubleToString(m1: String, m2: String): String {
        val p1 = BigDecimal(AppUtil.replaceComma(m1))
        val p2 = BigDecimal(AppUtil.replaceComma(m2))
        return p1.add(p2).stripTrailingZeros().toPlainString()
    }

    /**
     * double类型的加法运算（需要舍入，保留三位小数）
     *
     * @param m1
     * @param m2
     * @return 不加doubleValue()则, 返回BigDecimal对象
     */
    fun additionDouble(m1: Double, m2: Double, scale: Int): Double {
        val p1 = BigDecimal(m1.toString())
        val p2 = BigDecimal(m2.toString())
        return p1.add(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * double类型的超大数值加法运算（需要舍入，保留三位小数）
     *
     * @param m1
     * @param m2
     * @return 不加doubleValue()则, 返回BigDecimal对象
     */
    fun additionDoubleToStr(m1: Double, m2: Double, scale: Int): String {
        val p1 = BigDecimal(m1.toString())
        val p2 = BigDecimal(m2.toString())
        return p1.add(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString()
    }

    /**
     * double类型的减法运算
     *
     * @param m1
     * @param m2
     * @return 不加doubleValue()则, 返回BigDecimal对象
     */
    fun subtractionDouble(m1: Double, m2: Double): Double {
        val p1 = BigDecimal(m1.toString())
        val p2 = BigDecimal(m2.toString())
        return p1.subtract(p2).toDouble()
    }

    /**
     * double类型的加法运算（不需要舍入）
     *
     * @param m1
     * @param m2
     * @return 不加doubleValue()则, 返回BigDecimal对象
     */
    fun subtractionDoubleToString(m1: Double, m2: Double): String {
        val p1 = BigDecimal(AppUtil.replaceComma(m1.toString()))
        val p2 = BigDecimal(AppUtil.replaceComma(m2.toString()))
        return p1.subtract(p2).stripTrailingZeros().toPlainString()
    }

    /**
     * double类型的减法运算（需要舍入，保留三位小数）
     *
     * @param m1
     * @param m2
     * @return 不加doubleValue()则, 返回BigDecimal对象
     */
    fun subtractionDouble(m1: Double, m2: Double, scale: Int): Double {
        val p1 = BigDecimal(m1.toString())
        val p2 = BigDecimal(m2.toString())
        return p1.subtract(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * double类型的超大数值减法运算（需要舍入，保留三位小数）
     *
     * @param m1
     * @param m2
     * @return 不加doubleValue()则, 返回BigDecimal对象
     */
    fun subtractionDoubleToStr(m1: Double, m2: Double, scale: Int): String {
        val p1 = BigDecimal(m1.toString())
        val p2 = BigDecimal(m2.toString())
        return p1.subtract(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString()
    }

    /**
     * double类型的乘法运算
     *
     * @param m1
     * @param m2
     * @return 不加doubleValue()则, 返回BigDecimal对象
     */
    fun multiplicationDouble(m1: Double, m2: Double): Double {
        val p1 = BigDecimal(m1.toString())
        val p2 = BigDecimal(m2.toString())
        return p1.multiply(p2).toDouble()
    }

    /**
     * double类型的乘法运算
     *
     * @param m1
     * @param m2
     * @return 不加doubleValue()则, 返回BigDecimal对象
     */
    fun multiplicationDouble(m1: Double, m2: Double, scale: Int): Double {
        val p1 = BigDecimal(m1.toString())
        val p2 = BigDecimal(m2.toString())
        return p1.multiply(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * double类型的超大数值的乘法运算
     *
     * @param m1
     * @param m2
     * @return 不加doubleValue()则, 返回BigDecimal对象
     */
    fun multiplicationDoubleToStr(m1: Double, m2: Double, scale: Int): String {
        val p1 = BigDecimal(m1.toString())
        val p2 = BigDecimal(m2.toString())
        return p1.multiply(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString()
    }

    /**
     * double类型的除法运算
     *
     * @param m1
     * @param m2
     * @param scale
     * @return 不加doubleValue()则, 返回BigDecimal对象
     */
    fun divisionDouble(m1: Double, m2: Double, scale: Int): Double {
        require(scale >= 0) { "Parameter error" }
        val p1 = BigDecimal(m1.toString())
        val p2 = BigDecimal(m2.toString())
        return p1.divide(p2, scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * double类型的超大数值的除法运算
     *
     * @param m1
     * @param m2
     * @param scale
     * @return 不加doubleValue()则, 返回BigDecimal对象
     */
    fun divisionDoubleToStr(m1: Double, m2: Double, scale: Int): String {
        require(scale >= 0) { "Parameter error" }
        val p1 = BigDecimal(m1.toString())
        val p2 = BigDecimal(m2.toString())
        return p1.divide(p2, scale, BigDecimal.ROUND_HALF_UP).toPlainString()
    }

    /**
     * double类型的超大数值的除法运算
     *
     * @param m1
     * @param m2
     * @param scale
     * @return 不加doubleValue()则, 返回BigDecimal对象
     */
    fun divisionDoubleToString(m1: Double, m2: Double): String {
        val p1 = BigDecimal(AppUtil.replaceComma(m1.toString()))
        val p2 = BigDecimal(AppUtil.replaceComma(m2.toString()))
        return p1.divide(p2,0,RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
    }
}