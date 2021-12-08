package net.ntworld.foundation.internal

import net.ntworld.foundation.MessageAttribute

internal data class MessageAttributeImpl(
    override val dataType: String,
    override val binaryValue: ByteArray?,
    override val stringValue: String?
) : MessageAttribute