package com.bhaakl.anisy.presentation.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.viewbinding.ViewBinding
import com.bhaakl.anisy.domain.model.UiLoadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment<ViewModel : BaseViewModel, Binding : ViewBinding>(
    @LayoutRes layoutId: Int
) : Fragment(layoutId) {

    protected abstract val viewModel: ViewModel
    protected abstract val binding: Binding

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()
        setupListeners()
        setupRequests()
        setupSubscribers()
    }

    protected open fun initialize() {
    }

    protected open fun setupListeners() {
    }

    protected open fun setupRequests() {
    }

    protected open fun setupSubscribers() {
    }

    /**
     * Collect flow safely with [repeatOnLifecycle] API
     */
    protected fun collectFlowSafely(
        lifecycleState: Lifecycle.State, collect: suspend () -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(lifecycleState) {
                collect()
            }
        }
    }

    /**
     * Collect flow safely with [repeatOnLifecycle] API
     */
    protected fun <T> LiveData<T>.observeSafely(
        lifecycleOwner: LifecycleOwner, lifecycleState: Lifecycle.State, observer: Observer<T>
    ) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(lifecycleState) {
                observe(lifecycleOwner, observer)
            }
        }
    }

    /**
     * Observe [UiLoadState] with [observeSafely] and optional states params
     * @param state for working with all states
     * @param onError for error handling
     * @param onSuccess for working with data
     */
    protected fun <T> LiveData<UiLoadState<T>>.observeUIState(
        lifecycleOwner: LifecycleOwner,
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        state: ((UiLoadState<T>) -> Unit)? = null,
        onError: ((Pair<Int?, String?>) -> Unit),
        onLoading: (() -> Unit)? = null,
        onSuccess: ((data: T) -> Unit)
    ) {
        observeSafely(lifecycleOwner, lifecycleState) { uiState ->
            state?.invoke(uiState)
            when (uiState) {
                is UiLoadState.Loading -> onLoading?.invoke()

                is UiLoadState.DataError -> if (uiState.errorCode == null && !uiState.description.isNullOrBlank()) onError.invoke(
                    Pair(null, uiState.description)
                ) else onError.invoke(Pair(uiState.errorCode, null))

                is UiLoadState.Success -> uiState.data?.let { onSuccess.invoke(it) }
            }
        }
    }

    /**
     * Setup views visibility depending on [UiLoadState] states.
     * @param isShowViewIfSuccess is responsible for displaying views depending on whether
     * to navigate further or stay this Fragment
     */
    fun <T> UiLoadState<T>.setupViewVisibility(
//        group: Group,
        loader: FrameLayout, isShowViewIfSuccess: Boolean = false
    ) {
        fun showLoader(isVisible: Boolean) {
//            group.isVisible = !isVisible
            loader.isVisible = isVisible
        }

        when (this) {
            is UiLoadState.Loading -> showLoader(true)
            is UiLoadState.DataError -> showLoader(false)
            is UiLoadState.Success -> if (!isShowViewIfSuccess) showLoader(false)
        }
    }

    /**
     * Collect [PagingData] with [collectFlowSafely]
     */
    protected fun <T : Any> Flow<PagingData<T>>.collectPaging(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        action: suspend (value: PagingData<T>) -> Unit
    ) {
        collectFlowSafely(lifecycleState) { this.collectLatest { action(it) } }
    }
}