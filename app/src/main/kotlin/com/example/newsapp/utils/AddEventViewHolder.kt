package com.example.newsapp.utils

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R

class AddEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textEventShowing: TextView = itemView.findViewById(R.id.textEventShowing)
}