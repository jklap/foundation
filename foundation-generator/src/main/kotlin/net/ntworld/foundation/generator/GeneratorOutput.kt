package net.ntworld.foundation.generator

import com.squareup.kotlinpoet.FileSpec
import java.text.SimpleDateFormat
import java.util.*

object GeneratorOutput {
    private var isTest = false

    fun setupTest() {
        isTest = true
    }

    fun tearDownTest() {
        isTest = false
    }

    fun addToolHeader(file: FileSpec.Builder, generator: String?) {
        file.addFileComment("+-------------------------------------------------------------------------+\n")
        file.addFileComment("| This file was generated automatically by tools in foundation-generator. |\n")
        file.addFileComment("|                                                                         |\n")
        file.addFileComment("| Please do not edit!                                                     |\n")
        file.addFileComment("+-------------------------------------------------------------------------+\n")
        file.addFileComment("+- by  : $generator\n")
        file.addFileComment("+- when: ${now()}")
    }

    fun addHeader(file: FileSpec.Builder, generator: String?) {
        if (isTest) {
            return
        }

        file.addFileComment("+--------------------------------------------------------+\n")
        file.addFileComment("| This file was generated automatically in compile time. |\n")
        file.addFileComment("|                                                        |\n")
        file.addFileComment("| Please do not edit!                                    |\n")
        file.addFileComment("+--------------------------------------------------------+\n")
        file.addFileComment("+- by  : $generator\n")
        file.addFileComment("+- when: ${now()}")
    }

    private fun now(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(Date())
    }
}