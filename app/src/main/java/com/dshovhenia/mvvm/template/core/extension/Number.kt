package com.dshovhenia.mvvm.template.core.extension

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun Number.formatPrice(): String {
    val numberFormat = NumberFormat.getCurrencyInstance()
    numberFormat.currency = Currency.getInstance(Locale.US)
    numberFormat.minimumFractionDigits = 2
    numberFormat.maximumFractionDigits = 8
    return numberFormat.format(this)
}
