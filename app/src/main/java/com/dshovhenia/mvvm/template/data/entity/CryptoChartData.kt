package com.dshovhenia.mvvm.template.data.entity

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class CryptoChartData(
    @SerializedName("market_caps")
    val marketCaps: List<List<BigDecimal?>>,
    @SerializedName("prices")
    val prices: List<List<BigDecimal?>>,
    @SerializedName("total_volumes")
    val totalVolumes: List<List<BigDecimal?>>,
)
