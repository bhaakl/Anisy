package com.bhaakl.newsapp.presentation.ui.component.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bhaakl.newsapp.R

class CalendarAdapter(
    private val daysOfMonth: List<String>,
    private val onItemClickListener: (Int, String) -> Unit
) : RecyclerView.Adapter<CalendarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_cell, parent, false)
        view.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        view.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        return CalendarViewHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val dayText = daysOfMonth[position]
        if (dayText.contains('f')) {
            val newDayText = dayText.substring(0, dayText.length - 1)
            holder.dayOfMonth.text = newDayText
            holder.dayOfMonth.setTextColor(
                getColor(
                    holder.itemView.context,
                    R.color.my_gray_color
                )
            )
        } else if (dayText.contains('t')) {
            val newDayText = dayText.substring(0, dayText.length - 1)
            holder.dayOfMonth.text = newDayText
            holder.dayOfMonth.setStrokeColorResource(R.color.my_color_500)
        } else {
            holder.dayOfMonth.text = dayText
        }
    }

    override fun getItemCount(): Int = daysOfMonth.size
}