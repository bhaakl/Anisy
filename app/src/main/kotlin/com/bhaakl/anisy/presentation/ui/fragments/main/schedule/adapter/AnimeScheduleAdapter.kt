package com.bhaakl.anisy.presentation.ui.fragments.main.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bhaakl.anisy.R
import com.bhaakl.anisy.databinding.ScheduleItemBinding
import com.bhaakl.anisy.presentation.base.BaseDiffUtilItemCallback
import com.bhaakl.anisy.presentation.model.anime.AnimeUi
import com.squareup.picasso.Picasso

class AnimeScheduleAdapter(/*private val openNewsDetails: (anime: AnimeUi) -> Unit*/) :
    PagingDataAdapter<AnimeUi, AnimeScheduleAdapter.AnimeSchedulePagingViewHolder>(
        BaseDiffUtilItemCallback(),
    ) {

    /*private val onItemClickListener: RecyclerItemListener = object : RecyclerItemListener {
        override fun onItemSelected(anime: AnimeUi) {
            openNewsDetails(anime)
        }
    }*/

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnimeSchedulePagingViewHolder {
        val itemBinding =
            ScheduleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeSchedulePagingViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AnimeSchedulePagingViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    inner class AnimeSchedulePagingViewHolder(private val binding: ScheduleItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun onBind(anime: AnimeUi) = with(binding) {
            Picasso.get()
                .load(anime.images?.jpg?.largeImageUrl ?: anime.images?.jpg?.imageUrl)
                .resize(52, 64)
                .centerCrop()
                .placeholder(R.drawable.not_available)
                .error(R.drawable.not_available).into(ivScheduleAvatar)

            tvScheduleName.text = anime.titles?.find { it.type == "Default" }?.title ?: anime.titles?.find { it.type == "Japanese" }?.title
            /*lvNewsItemCard.setOnClickListener {
                recyclerItemListener.onItemSelected(
                    anime
                )
            }*/
        }
    }
}