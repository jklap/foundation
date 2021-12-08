package net.ntworld.foundation

interface MessageAttribute {
    val dataType: String

    val binaryValue: ByteArray?

    val stringValue: String?
}