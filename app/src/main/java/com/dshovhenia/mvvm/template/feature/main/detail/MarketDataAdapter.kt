package com.dshovhenia.mvvm.template.feature.main.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dshovhenia.mvvm.template.core.extension.simpleItemCallback
import com.dshovhenia.mvvm.template.databinding.ViewHolderMarketDataItemBinding

class MarketDataAdapter : ListAdapter<Pair<String, String>, MarketDataAdapter.ViewHolder>(simpleItemCallback()) {
    class ViewHolder(val binding: ViewHolderMarketDataItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pair<String, String>) {
            binding.tvMarketDataDescription.text = item.first
            binding.tvMarketDataValue.text = item.second
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ViewHolderMarketDataItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        holder.bind(item)
    }
}
