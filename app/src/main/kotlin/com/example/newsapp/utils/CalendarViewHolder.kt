package com.example.newsapp.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.google.android.material.button.MaterialButton

class CalendarViewHolder(itemView: View, private val onItemClickListener: (Int, String) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    val dayOfMonth: MaterialButton = itemView.findViewById(R.id.cellDayText)

    init {
        dayOfMonth.setOnClickListener {
            onItemClickListener(adapterPosition, dayOfMonth.text.toString())
        }
    }
}