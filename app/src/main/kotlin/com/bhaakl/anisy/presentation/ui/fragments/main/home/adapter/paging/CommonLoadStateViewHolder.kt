package com.bhaakl.anisy.presentation.ui.fragments.main.home.adapter.paging

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.bhaakl.anisy.R
import com.bhaakl.anisy.databinding.LoadStateFooterViewItemBinding
import com.bhaakl.anisy.presentation.base.show.ShowerMessage

class CommonLoadStateViewHolder(
    private val binding: LoadStateFooterViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun onBind(loadState: LoadState, showSnackbarMsg: ShowerMessage) = with(binding) {
        Log.d("CommonLoadStateViewHolder", "bind: $loadState")
        if (loadState is LoadState.Error) {
            loadState.error.localizedMessage?.let { showSnackbarMsg.showMessage(it) }
            errorMsg.text = loadState.error.localizedMessage
        }

        progressBar.isVisible = loadState is LoadState.Loading
        retryButton.isVisible = loadState is LoadState.Error
        errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): CommonLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.load_state_footer_view_item, parent, false)
            val binding = LoadStateFooterViewItemBinding.bind(view)
            return CommonLoadStateViewHolder(binding, retry)
        }
    }
}