package com.trivaris.kenmei.model.types

fun Long?.toBoolean() = this == 1L
fun Boolean?.toLong() = if (this == true) 1L else 0L
