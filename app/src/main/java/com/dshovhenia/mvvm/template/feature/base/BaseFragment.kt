package com.dshovhenia.mvvm.template.feature.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.dshovhenia.mvvm.template.domain.error.AppError

@SuppressLint("Registered")
abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    val binding get() = requireNotNull(_binding)

    /**
     * Mandatory function which returns inflated view-binding of a fragment.
     *
     * Example: return FragmentAbcBinding.inflate(inflater, container, false)
     */
    abstract fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = provideViewBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun handleFailure(errorEvent: AppError) {
        (requireActivity() as? BaseActivity<*>)?.handleFailure(errorEvent)
    }
}
