package com.dshovhenia.mvvm.template.feature.main.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dshovhenia.mvvm.template.core.extension.addLinearLayoutManager
import com.dshovhenia.mvvm.template.core.extension.gone
import com.dshovhenia.mvvm.template.core.extension.observe
import com.dshovhenia.mvvm.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.template.databinding.FragmentCoinsListBinding
import com.dshovhenia.mvvm.template.feature.base.BaseFragment
import com.dshovhenia.mvvm.template.feature.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CoinsListFragment : BaseFragment<FragmentCoinsListBinding>() {

    private val viewModel: MainViewModel by activityViewModel()
    private val coinListAdapter = CoinListAdapter(::onCoinClick)

    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCoinsListBinding {
        return FragmentCoinsListBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observeEvents()

        viewModel.getCoinsMarkets()
    }

    private fun setupAdapter() {
        binding.recyclerView.addLinearLayoutManager()
        binding.recyclerView.adapter = coinListAdapter
    }

    private fun observeEvents() {
        observe(viewModel.coins) {
            binding.progress.gone()
            coinListAdapter.submitList(it)
        }
    }

    private fun onCoinClick(coin: CoinMarkets) {
        viewModel.openDetailScreen(coin)
    }
}
