package com.dshovhenia.mvvm.template.feature.main.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.dshovhenia.mvvm.template.R
import com.dshovhenia.mvvm.template.feature.main.detail.CoinDetailFragment
import com.dshovhenia.mvvm.template.widget.chart.CustomMarkerView
import com.dshovhenia.mvvm.template.widget.chart.XAxisValueFormatter
import com.dshovhenia.mvvm.template.widget.chart.YAxisValueFormatter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import java.math.BigDecimal

class ChartView(
    context: Context,
    attrs: AttributeSet?
) : LineChart(context, attrs) {

    init {
        initChart()
    }

    private fun initChart() {
        setViewPortOffsets(0f, 0f, 0f, 0f)

        setNoDataText(context.getString(R.string.building_chart))
        // description text
        description.text = context.getString(R.string.price_usd_vs_time)
        description.textSize = 18F
        description.textColor = Color.WHITE
        description.isEnabled = true

        // enable touch gestures
        setTouchEnabled(true)

        // enable scaling and dragging
        isDragEnabled = false
        setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        setPinchZoom(false)

        setDrawGridBackground(false)
        maxHighlightDistance = 300f

        val x = xAxis
        x.textColor = Color.WHITE
        x.axisLineColor = Color.WHITE
        x.position = XAxis.XAxisPosition.BOTTOM
        x.enableGridDashedLine(2f, 7f, 0f)
        x.setLabelCount(6, false)
        x.setDrawLabels(true)
        x.isEnabled = true
        x.setDrawAxisLine(true)
        x.setDrawGridLines(true)
        x.isGranularityEnabled = true
        x.granularity = 7f
        x.labelRotationAngle = 315f
        x.valueFormatter = XAxisValueFormatter()
        x.setCenterAxisLabels(false)
        x.setDrawLimitLinesBehindData(true)

        val y = axisLeft
        y.setLabelCount(6, false)
        y.setDrawAxisLine(true)
        y.textColor = Color.WHITE
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        y.setDrawGridLines(false)
        y.valueFormatter = YAxisValueFormatter()
        y.axisLineColor = Color.WHITE
        y.setDrawZeroLine(false)
        y.setDrawLimitLinesBehindData(false)

        axisRight.isEnabled = false
        legend.isEnabled = false
        animateX(2000)
        // set marker
        val customMarker = CustomMarkerView(context)
        marker = customMarker
    }

    fun setChartData(chartFillData: List<List<BigDecimal>>) {
        val values = ArrayList<Entry>()
        for (i in chartFillData.indices) {
            val timestamp = chartFillData[i][0].toFloat()
            val price = chartFillData[i][1].toFloat()

            values.add(Entry(timestamp, price))
        }
        val set: LineDataSet
        if (data != null && data.dataSetCount > 0) {
            set = data.getDataSetByIndex(0) as LineDataSet
            set.values = values
            data.notifyDataChanged()
            notifyDataSetChanged()
        } else {
            // create a data set and give it a type
            set = LineDataSet(values, CoinDetailFragment.DATA_SET_NAME)
            set.mode = LineDataSet.Mode.CUBIC_BEZIER
            set.cubicIntensity = 0.2f
            set.setDrawFilled(true)
            set.setDrawCircles(false)
            set.lineWidth = 1.8f
            set.circleRadius = 4f
            set.setCircleColor(Color.BLUE)
            set.highLightColor = Color.rgb(244, 117, 117)
            set.color = ContextCompat.getColor(context, R.color.chart_color)
            set.fillColor = ContextCompat.getColor(context, R.color.chart_fill_color)
            set.setDrawHorizontalHighlightIndicator(true)
            set.fillFormatter = IFillFormatter { _, _ -> axisLeft.axisMinimum }

            // create a data object with the data sets
            val lineData = LineData(set)
            lineData.setValueTextSize(9f)
            lineData.setDrawValues(false)

            setData(lineData)
        }
        invalidate()
    }
}
