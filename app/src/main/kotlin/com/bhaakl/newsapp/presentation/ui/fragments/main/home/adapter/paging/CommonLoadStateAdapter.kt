package com.bhaakl.newsapp.presentation.ui.fragments.main.home.adapter.paging

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.bhaakl.newsapp.presentation.base.show.ShowerMessage
import kotlin.reflect.KFunction1

class CommonLoadStateAdapter(
    private val retry: () -> Unit,
    private val showSnackBar: (m: String) -> Unit
) : LoadStateAdapter<CommonLoadStateViewHolder>() {

    private val showMsgInSnackBar: ShowerMessage = object : ShowerMessage {
        override fun showMessage(msg: String) {
            showSnackBar(msg)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ): CommonLoadStateViewHolder {
        return CommonLoadStateViewHolder.create(parent, retry)
    }

    override fun onBindViewHolder(holder: CommonLoadStateViewHolder, loadState: LoadState) {
        holder.onBind(loadState, showMsgInSnackBar)
    }
}