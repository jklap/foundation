package net.ntworld.foundation.eventSourcing

@Target(AnnotationTarget.FIELD)
annotation class Encrypted(val faked: String = "")
