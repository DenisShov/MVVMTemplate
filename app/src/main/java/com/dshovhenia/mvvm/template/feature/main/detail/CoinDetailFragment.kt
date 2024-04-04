package com.dshovhenia.mvvm.template.feature.main.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dshovhenia.mvvm.template.Constants
import com.dshovhenia.mvvm.template.R
import com.dshovhenia.mvvm.template.core.extension.addLinearLayoutManager
import com.dshovhenia.mvvm.template.core.extension.formatPrice
import com.dshovhenia.mvvm.template.core.extension.observe
import com.dshovhenia.mvvm.template.core.extension.toLocalDateTime
import com.dshovhenia.mvvm.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.template.databinding.FragmentCoinDetailBinding
import com.dshovhenia.mvvm.template.feature.base.BaseFragment
import com.dshovhenia.mvvm.template.feature.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

class CoinDetailFragment : BaseFragment<FragmentCoinDetailBinding>() {

    @Suppress("DEPRECATION")
    private val coin: CoinMarkets by lazy { arguments?.getParcelable(ARG_COIN)!! }

    private val viewModel: MainViewModel by activityViewModel()

    private val marketDataAdapter = MarketDataAdapter()
    private lateinit var selectedTextViewTimeFilter: View

    private val onTimePeriodSelected = View.OnClickListener { view ->
        view?.let {
            when (it.id) {
                binding.tv1h.id -> selectTimePeriod(it, Constants.ONE_HOUR)
                binding.tv24hr.id -> selectTimePeriod(it, Constants.ONE_DAY)
                binding.tv7d.id -> selectTimePeriod(it, Constants.ONE_WEEK)
                binding.tv1m.id -> selectTimePeriod(it, Constants.ONE_MONTH)
                binding.tv3m.id -> selectTimePeriod(it, Constants.ONE_QUARTER)
                binding.tv1y.id -> selectTimePeriod(it, Constants.ONE_YEAR)
                binding.tvAllTime.id -> selectTimePeriod(it, Constants.MAX)
            }
        }
    }

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
            binding.chartView.setChartData(it.prices)
        }
    }

    private fun init() {
        initView()
        initViewModel()
        initMarketData()
    }

    private fun initView() {
        val timePeriodSelectorButtons = listOf(
            binding.tv1h, binding.tv24hr, binding.tv7d, binding.tv1m, binding.tv3m, binding.tv1y, binding.tvAllTime
        )
        for (timePeriodButton in timePeriodSelectorButtons) {
            timePeriodButton.setOnClickListener(onTimePeriodSelected)
        }

        binding.tvCoinName.text = coin.name
        binding.tvCoinPrice.text = coin.currentPrice?.formatPrice()
    }

    private fun initViewModel() {
        // default day selection
        binding.tv24hr.isSelected = true
        selectedTextViewTimeFilter = binding.tv24hr
        viewModel.initCryptoChart(coin.id)
    }

    private fun initMarketData() {
        binding.recyclerView.addLinearLayoutManager()
        binding.recyclerView.adapter = marketDataAdapter
        val data = with(coin) {
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

    private fun selectTimePeriod(view: View, days: String) {
        selectedTextViewTimeFilter.isSelected = false
        view.isSelected = true
        selectedTextViewTimeFilter = view
        viewModel.getCryptoChart(days)
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
