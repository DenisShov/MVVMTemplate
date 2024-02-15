package com.dshovhenia.mvvm.template.core.extension

import timber.log.Timber
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun String.toLocalDateTime(): LocalDateTime? {
    return try {
        Instant.parse(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}
