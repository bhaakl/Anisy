package com.bhaakl.newsapp.presentation.ui.component.schedule.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bhaakl.newsapp.R

class AddEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textEventShowing: TextView = itemView.findViewById(R.id.textEventShowing)
}