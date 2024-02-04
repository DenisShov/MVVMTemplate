package com.dshovhenia.mvvm.template.feature.main.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.dshovhenia.mvvm.template.core.extension.formatPrice
import com.dshovhenia.mvvm.template.core.extension.simpleItemCallback
import com.dshovhenia.mvvm.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.template.databinding.ViewHolderCoinItemBinding

class CoinListAdapter(
    private var onClickListener: ((coin: CoinMarkets) -> Unit)? = null,
) : ListAdapter<CoinMarkets, CoinListAdapter.ViewHolder>(simpleItemCallback()) {
    class ViewHolder(val binding: ViewHolderCoinItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: CoinMarkets,
            onClick: (CoinMarkets) -> Unit,
        ) {
            binding.tvCoinName.text = item.name
            val price = "${item.currentPrice?.formatPrice()} USD"
            binding.tvCoinPrice.text = price

            Glide.with(binding.root.context)
                .load(item.image)
                .transform(MultiTransformation(CircleCrop()))
                .into(binding.ivCoinImage)

            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ViewHolderCoinItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        holder.bind(item, ::onClick)
    }

    private fun onClick(category: CoinMarkets) {
        onClickListener?.invoke(category)
    }
}
