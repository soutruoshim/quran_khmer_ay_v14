package com.sout_rahim.quran_ay.util

class ArabConvertNumber(private val input: String) {
    val output: String

    init {
        this.output = normalize()
    }

    private fun normalize(): String {
        val arabic_zero_unicode = 1632
        val str = input
        if (input == Constants.ZERO_STRING) return " " // Return space if input is "0"
        val builder = StringBuilder()
        for (i in 0 until str.length) {
            builder.append((str[i].code - 48 + arabic_zero_unicode).toChar())
        }
        return builder.toString()
    }
}