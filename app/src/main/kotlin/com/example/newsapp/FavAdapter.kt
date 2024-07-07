package com.example.newsapp

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.models.FavouriteData
import com.example.newsapp.databinding.ItemFavouriteBinding

class FavAdapter(private val context: Context, private val list: ArrayList<FavouriteData>) :
    RecyclerView.Adapter<FavAdapter.FavViewHolder>() {

    class FavViewHolder(val binding: ItemFavouriteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val binding = ItemFavouriteBinding.inflate(LayoutInflater.from(context), parent, false)
        return FavViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val item = list[position]
        holder.binding.favTitle.text = item.title
        holder.binding.favContent.text = "${item.content}\n"
        holder.binding.favTitle.setOnClickListener {
            val f = Favourite()
            f.removeFav(item.title, "${item.content}\n", context)
        }
    }

    override fun getItemCount(): Int {
        return list.size - list.size / 2
    }

    companion object {
        var dbName: String = "favDb"
    }
}