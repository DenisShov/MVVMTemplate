package com.dshovhenia.mvvm.template.core.extension

import android.annotation.SuppressLint
import com.dshovhenia.mvvm.template.Constants
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.String

@SuppressLint("SimpleDateFormat")
fun Float.formatDate(): String {
    val date = Date(this.toLong())
    val sdf = SimpleDateFormat(Constants.DATE_FORMAT)
    return sdf.format(date)
}
