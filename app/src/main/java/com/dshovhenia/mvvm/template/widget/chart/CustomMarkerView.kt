package com.dshovhenia.mvvm.template.widget.chart

import android.content.Context
import android.view.LayoutInflater
import com.dshovhenia.mvvm.template.R
import com.dshovhenia.mvvm.template.core.extension.formatDate
import com.dshovhenia.mvvm.template.core.extension.formatPrice
import com.dshovhenia.mvvm.template.databinding.CustomMarkerViewBinding
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class CustomMarkerView(context: Context?) : MarkerView(context, R.layout.custom_marker_view) {

    private var binding: CustomMarkerViewBinding
    private var mOffset: MPPointF? = null

    init {
        binding = CustomMarkerViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun refreshContent(e: Entry, highlight: Highlight) {
        val priceAtPoint = "${e.y.formatPrice()}\n${e.x.formatDate()}"
        binding.textViewEntry.text = priceAtPoint
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF? {
        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = MPPointF(-(width / 2).toFloat(), (-2 * height).toFloat())
        }
        return mOffset
    }
}
