package com.dshovhenia.mvvm.template.feature.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.dshovhenia.mvvm.template.R
import com.dshovhenia.mvvm.template.core.extension.observeEvents
import com.dshovhenia.mvvm.template.databinding.ActivityMainBinding
import com.dshovhenia.mvvm.template.feature.base.BaseActivity
import com.dshovhenia.mvvm.template.feature.main.detail.CoinDetailFragment.Companion.ARG_COIN
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: MainViewModel by viewModel()
    private val navController: NavController get() = findNavController(R.id.nav_main_fragment)

    override fun provideViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        observeEvents(viewModel.event) {
            when (it) {
                is MainViewModel.Event.ShowDetailScreen -> {
                    navigateToCoinDetail(it)
                }
            }
        }
        observeEvents(viewModel.failure) {
            handleFailure(it)
        }
    }

    private fun navigateToCoinDetail(it: MainViewModel.Event.ShowDetailScreen) {
        val args = bundleOf(ARG_COIN to it.coin)
        navController.navigate(R.id.detailFragment, args)
    }
}
