package com.bhaakl.newsapp.presentation.ui.fragments.main.schedule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getColorStateList
import androidx.recyclerview.widget.RecyclerView
import com.bhaakl.newsapp.R
import com.bhaakl.newsapp.databinding.AnimeItemBinding
import com.bhaakl.newsapp.databinding.CalendarCellBinding

class CalendarAdapter(
    private val daysOfMonthList: List<String>,
    private val context: Context,
    private val onItemClickListener: (String) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val itemBinding =
            CalendarCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        itemBinding.root.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        itemBinding.root.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        return CalendarViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        daysOfMonthList[position].let{ holder.onBind(it, onItemClickListener) }
    }

    override fun getItemCount(): Int = daysOfMonthList.size

    inner class CalendarViewHolder(
        private val binding: CalendarCellBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(dayText: String, onItemClickListener: (String) -> Unit) = with(binding) {
            if (dayText.contains('f')) {
                val newDayText = dayText.substring(0, dayText.length - 1)
                cellDayText.text = newDayText
                cellDayText.setTextColor(
                    getColor(
                        context,
                        R.color.my_gray_color
                    )
                )
            } else if (dayText.contains('t')) {
                val newDayText = dayText.substring(0, dayText.length - 1)
                cellDayText.text = newDayText
                cellDayText.setStrokeColorResource(R.color.my_color_500)
            } else {
                cellDayText.text = dayText
            }

            cellDayText.setOnClickListener {
                onItemClickListener(cellDayText.text.toString())
            }
        }
    }
}