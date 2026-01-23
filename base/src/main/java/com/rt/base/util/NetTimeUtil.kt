package com.rt.base.util

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

object NetTimeUtil {

    fun getNtpTime(): Long {
        return try {
            val address = InetAddress.getByName("ntp.aliyun.com")
            val buffer = ByteArray(48)
            buffer[0] = 0x1B

            val socket = DatagramSocket()
            socket.soTimeout = 3000
            val request = DatagramPacket(buffer, buffer.size, address, 123)
            socket.send(request)

            val response = DatagramPacket(buffer, buffer.size)
            socket.receive(response)

            socket.close()

            val transmitTime = readTimestamp(buffer, 40)
            transmitTime - 2208988800L * 1000L // NTP时间戳转为Unix时间戳（毫秒）
        } catch (e: Exception) {
            e.printStackTrace()
            System.currentTimeMillis()
        }
    }

    private fun readTimestamp(buffer: ByteArray, pointer: Int): Long {
        var seconds = 0L
        var fraction = 0L
        for (i in 0..3) {
            seconds = (seconds shl 8) or (buffer[pointer + i].toLong() and 0xff)
        }
        for (i in 4..7) {
            fraction = (fraction shl 8) or (buffer[pointer + i].toLong() and 0xff)
        }
        return (seconds * 1000) + (fraction * 1000) / 0x100000000L
    }
}
