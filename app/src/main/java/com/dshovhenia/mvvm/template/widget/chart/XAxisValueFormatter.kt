package com.dshovhenia.mvvm.template.widget.chart

import com.dshovhenia.mvvm.template.core.extension.formatDate
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class XAxisValueFormatter : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase): String {
        return value.formatDate()
    }
}
