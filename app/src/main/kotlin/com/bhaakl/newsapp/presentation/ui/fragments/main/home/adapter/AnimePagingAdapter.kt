package com.bhaakl.newsapp.presentation.ui.fragments.main.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bhaakl.newsapp.R
import com.bhaakl.newsapp.databinding.AnimeItemBinding
import com.bhaakl.newsapp.presentation.base.BaseDiffUtilItemCallback
import com.bhaakl.newsapp.presentation.base.listeners.RecyclerItemListener
import com.bhaakl.newsapp.presentation.model.AnimeUi
import com.squareup.picasso.Picasso

class AnimePagingAdapter(private val openNewsDetails: (anime: AnimeUi) -> Unit) : PagingDataAdapter<AnimeUi, AnimePagingAdapter.AnimePagingViewHolder>(
    BaseDiffUtilItemCallback(),
) {

    private val onItemClickListener: RecyclerItemListener = object : RecyclerItemListener {
        override fun onItemSelected(anime: AnimeUi) {
            openNewsDetails(anime)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimePagingViewHolder {
        val itemBinding =
            AnimeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimePagingViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AnimePagingViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it, onItemClickListener) }
    }

    inner class AnimePagingViewHolder(private val binding: AnimeItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {

        fun onBind(anime: AnimeUi, recyclerItemListener: RecyclerItemListener) = with(binding) {
            tvName.text = anime.titles?.firstOrNull()?.title
            if (!anime.background.isNullOrBlank()) {
                tvCaption.text = anime.background
            } else tvCaption.visibility = View.GONE
            tvAnimeYear.text = binding.root.context.getString(R.string.anime_aired, anime.aired?.string ?: "")
            Picasso.get()
                .load(anime.images?.jpg?.largeImageUrl  ?: anime.images?.jpg?.imageUrl)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.not_available)
                .error(R.drawable.not_available).into(ivAnimeImage)
            cardAnimeItem.setOnClickListener {
                recyclerItemListener.onItemSelected(
                    anime
                )
            }
        }
    }
}