package com.dshovhenia.mvvm.template.core.extension

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import kotlin.String

fun Number.formatPrice(withoutSymbol: Boolean = false): String {
    val numberFormat = NumberFormat.getCurrencyInstance()
    numberFormat.currency = Currency.getInstance(Locale.US)
    numberFormat.minimumFractionDigits = 2
    numberFormat.maximumFractionDigits = 8
    val price = numberFormat.format(this)
    return if (withoutSymbol) {
        price.replace("$", "")
    } else {
        price
    }
}
