package com.dshovhenia.mvvm.template.feature.main.detail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.dshovhenia.mvvm.template.Constants
import com.dshovhenia.mvvm.template.R
import com.dshovhenia.mvvm.template.core.extension.addLinearLayoutManager
import com.dshovhenia.mvvm.template.core.extension.formatPrice
import com.dshovhenia.mvvm.template.core.extension.observe
import com.dshovhenia.mvvm.template.core.extension.toLocalDateTime
import com.dshovhenia.mvvm.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.template.data.entity.CryptoChartData
import com.dshovhenia.mvvm.template.databinding.FragmentCoinDetailBinding
import com.dshovhenia.mvvm.template.feature.base.BaseFragment
import com.dshovhenia.mvvm.template.feature.main.MainViewModel
import com.dshovhenia.mvvm.template.widget.chart.CustomMarkerView
import com.dshovhenia.mvvm.template.widget.chart.XAxisValueFormatter
import com.dshovhenia.mvvm.template.widget.chart.YAxisValueFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

class CoinDetailFragment : BaseFragment<FragmentCoinDetailBinding>(), View.OnClickListener {
    @Suppress("DEPRECATION")
    private val coin: CoinMarkets by lazy { arguments?.getParcelable(ARG_COIN)!! }
    private val viewModel: MainViewModel by activityViewModel()
    private lateinit var selectedTextViewTimeFilter: TextView

    private val marketDataAdapter = MarketDataAdapter()

    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCoinDetailBinding {
        return FragmentCoinDetailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observeEvents()
    }

    private fun observeEvents() {
        observe(viewModel.cryptoChartLiveData) {
            setChartData(it)
        }
    }

    private fun init() {
        initView()
        initViewModel()
        initChart()
        initMarketData()
    }

    private fun initView() {
        binding.tv1h.setOnClickListener(this)
        binding.tv24hr.setOnClickListener(this)
        binding.tv7d.setOnClickListener(this)
        binding.tv1m.setOnClickListener(this)
        binding.tv3m.setOnClickListener(this)
        binding.tv1y.setOnClickListener(this)
        binding.tvAllTime.setOnClickListener(this)

        binding.tvCoinName.text = coin.name
        binding.tvCoinPrice.text = coin.currentPrice?.formatPrice()
    }

    private fun initViewModel() {
        // default day selection
        binding.tv24hr.isSelected = true
        selectedTextViewTimeFilter = binding.tv24hr
        viewModel.initCryptoChart(coin.id)
    }

    private fun initChart() {
        binding.chart.setViewPortOffsets(0f, 0f, 0f, 0f)

        binding.chart.setNoDataText(getString(R.string.building_chart))
        // description text
        binding.chart.description.text = getString(R.string.price_usd_vs_time)
        binding.chart.description.textSize = 18F
        binding.chart.description.textColor = Color.WHITE
        binding.chart.description.isEnabled = true

        // enable touch gestures
        binding.chart.setTouchEnabled(true)

        // enable scaling and dragging
        binding.chart.isDragEnabled = false
        binding.chart.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        binding.chart.setPinchZoom(false)

        binding.chart.setDrawGridBackground(false)
        binding.chart.maxHighlightDistance = 300f

        val x = binding.chart.xAxis
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

        val y = binding.chart.axisLeft
        y.setLabelCount(6, false)
        y.setDrawAxisLine(true)
        y.textColor = Color.WHITE
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        y.setDrawGridLines(false)
        y.valueFormatter = YAxisValueFormatter()
        y.axisLineColor = Color.WHITE
        y.setDrawZeroLine(false)
        y.setDrawLimitLinesBehindData(false)

        binding.chart.axisRight.isEnabled = false
        binding.chart.legend.isEnabled = false
        binding.chart.animateX(2000)
        // set marker
        val customMarker = CustomMarkerView(requireContext())
        binding.chart.marker = customMarker
    }

    private fun initMarketData() {
        binding.recyclerView.addLinearLayoutManager()
        binding.recyclerView.adapter = marketDataAdapter
        val data =
            with(coin) {
                listOf(
                    Pair(getString(R.string.market_cap), marketCap?.formatPrice().orNotAvailable()),
                    Pair(getString(R.string.trading_volume_24h), totalVolume?.formatPrice().orNotAvailable()),
                    Pair(getString(R.string.highest_price_24h), high24h?.formatPrice().orNotAvailable()),
                    Pair(getString(R.string.lowest_price_24h), low24h?.formatPrice().orNotAvailable()),
                    Pair(
                        getString(R.string.available_supply),
                        circulatingSupply?.formatPrice(withoutSymbol = true).orNotAvailable(),
                    ),
                    Pair(
                        getString(R.string.total_supply),
                        totalSupply?.formatPrice(withoutSymbol = true).orNotAvailable(),
                    ),
                    Pair(
                        getString(R.string.max_supply),
                        totalSupply?.formatPrice(withoutSymbol = true).orNotAvailable(),
                    ),
                    Pair(getString(R.string.all_time_high_price), ath?.formatPrice().orNotAvailable()),
                    Pair(
                        getString(R.string.all_time_high_date),
                        athDate?.toLocalDateTime()?.format(provideDateFormatter()).orNotAvailable(),
                    ),
                    Pair(getString(R.string.all_time_low_price), atl?.formatPrice().orNotAvailable()),
                    Pair(
                        getString(R.string.all_time_low_date),
                        atlDate?.toLocalDateTime()?.format(provideDateFormatter()).orNotAvailable(),
                    ),
                    Pair(
                        getString(R.string.last_updated),
                        lastUpdated?.toLocalDateTime()?.format(provideDateTimeFormatter()).orNotAvailable(),
                    ),
                )
            }
        marketDataAdapter.submitList(data)
    }

    private fun setChartData(cryptoChartData: CryptoChartData?) {
        val chartFillData = cryptoChartData?.prices
        if (chartFillData != null) {
            val values = ArrayList<Entry>()
            for (i in chartFillData.indices) {
                val timestamp = chartFillData[i][0]!!.toFloat()
                val price = chartFillData[i][1]!!.toFloat()

                values.add(Entry(timestamp, price))
            }
            val set: LineDataSet
            if (binding.chart.data != null && binding.chart.data.dataSetCount > 0) {
                set = binding.chart.data.getDataSetByIndex(0) as LineDataSet
                set.values = values
                binding.chart.data.notifyDataChanged()
                binding.chart.notifyDataSetChanged()
            } else {
                // create a data set and give it a type
                set = LineDataSet(values, DATA_SET_NAME)
                set.mode = LineDataSet.Mode.CUBIC_BEZIER
                set.cubicIntensity = 0.2f
                set.setDrawFilled(true)
                set.setDrawCircles(false)
                set.lineWidth = 1.8f
                set.circleRadius = 4f
                set.setCircleColor(Color.BLUE)
                set.highLightColor = Color.rgb(244, 117, 117)
                set.color = ContextCompat.getColor(requireContext(), R.color.chart_color)
                set.fillColor = ContextCompat.getColor(requireContext(), R.color.chart_fill_color)
                set.setDrawHorizontalHighlightIndicator(true)
                set.fillFormatter = IFillFormatter { _, _ -> binding.chart.axisLeft.axisMinimum }

                // create a data object with the data sets
                val data = LineData(set)
                data.setValueTextSize(9f)
                data.setDrawValues(false)

                binding.chart.data = data
            }
            binding.chart.invalidate()
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.tv1h.id -> {
                selectedTextViewTimeFilter.isSelected = false
                binding.tv1h.isSelected = true
                selectedTextViewTimeFilter = binding.tv1h
                viewModel.getCryptoChart(Constants.ONE_HOUR)
            }

            binding.tv24hr.id -> {
                selectedTextViewTimeFilter.isSelected = false
                binding.tv24hr.isSelected = true
                selectedTextViewTimeFilter = binding.tv24hr
                viewModel.getCryptoChart(Constants.ONE_DAY)
            }

            binding.tv7d.id -> {
                selectedTextViewTimeFilter.isSelected = false
                binding.tv7d.isSelected = true
                selectedTextViewTimeFilter = binding.tv7d
                viewModel.getCryptoChart(Constants.ONE_WEEK)
            }

            binding.tv1m.id -> {
                selectedTextViewTimeFilter.isSelected = false
                binding.tv1m.isSelected = true
                selectedTextViewTimeFilter = binding.tv1m
                viewModel.getCryptoChart(Constants.ONE_MONTH)
            }

            binding.tv3m.id -> {
                selectedTextViewTimeFilter.isSelected = false
                binding.tv3m.isSelected = true
                selectedTextViewTimeFilter = binding.tv3m
                viewModel.getCryptoChart(Constants.ONE_QUARTER)
            }

            binding.tv1y.id -> {
                selectedTextViewTimeFilter.isSelected = false
                binding.tv1y.isSelected = true
                selectedTextViewTimeFilter = binding.tv1y
                viewModel.getCryptoChart(Constants.ONE_YEAR)
            }

            binding.tvAllTime.id -> {
                selectedTextViewTimeFilter.isSelected = false
                binding.tvAllTime.isSelected = true
                selectedTextViewTimeFilter = binding.tvAllTime
                viewModel.getCryptoChart(Constants.MAX)
            }

            else -> {}
        }
    }

    private fun String?.orNotAvailable(): String {
        return this ?: resources.getString(R.string.not_available)
    }

    private fun provideDateFormatter() = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.US)

    private fun provideDateTimeFormatter() = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

    companion object {
        const val ARG_COIN = "coin"
        const val DATA_SET_NAME = "DataSet"
    }
}
