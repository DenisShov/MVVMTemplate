package com.dshovhenia.mvvm.template.feature.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.dshovhenia.mvvm.template.domain.error.AppError
import com.google.android.material.snackbar.Snackbar

@SuppressLint("Registered")
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    lateinit var binding: VB

    /**
     * Mandatory function which returns inflated view-binding of an activity.
     *
     * Example: return ActivityAbcBinding.inflate(inflater)
     */
    abstract fun provideViewBinding(inflater: LayoutInflater): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = provideViewBinding(layoutInflater)
        setContentView(binding.root)
    }

    fun handleFailure(error: AppError) {
        showLongSnackBar(binding.root, error.stringRes)
    }

    private fun showLongSnackBar(rootView: View, @StringRes messageRes: Int) {
        Snackbar.make(
            rootView,
            rootView.resources.getString(messageRes),
            Snackbar.LENGTH_LONG
        ).show()
    }
}
