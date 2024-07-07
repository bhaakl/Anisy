package com.example.newsapp

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.models.NewsHeadlines
import com.example.newsapp.databinding.HeadlineListItemsBinding

class CustomAdapter(
    private val context: Context,
    private val headlines: List<NewsHeadlines>,
    private val listener: SelectListener
) : RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {

    class CustomViewHolder(val binding: HeadlineListItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = HeadlineListItemsBinding.inflate(LayoutInflater.from(context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val headline = headlines[position]
        holder.binding.textTitle.text = headline.title
        holder.binding.textSource.text = headline.source?.name ?: ""

        Glide.with(context).load(headline.urlToImage).into(holder.binding.imgHeadline)
        holder.binding.mainContainer.setOnClickListener {
            listener.onNewsClicked(headline)
        }
    }

    override fun getItemCount(): Int {
        return headlines.size
    }
}

