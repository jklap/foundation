package net.ntworld.foundation

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object Base64 {
    fun encode(bytes: ByteArray): String

    fun decode(src: String): ByteArray
}
