package com.example.newsapp.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.models.CalEventText
import com.example.newsapp.room.repo.CalEventTextQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEventAdapter(
    private val calEventTextQuery: CalEventTextQuery,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<AddEventViewHolder>() {

    private var events: List<CalEventText> = emptyList()

    init {
        lifecycleScope.launch {
            try {
                calEventTextQuery.getAll().collect { newEvents ->
                    if (newEvents.isNotEmpty()) {
                        events = newEvents
                        notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                Log.e("PickEvent", "Ошибка при загрузке событий", e)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddEventViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.event_cards, parent, false)
        return AddEventViewHolder(itemView)
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: AddEventViewHolder, position: Int) {
        val eventText = events[position]
        holder.textEventShowing.text = eventText.text
    }

    fun addEvent(name: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val newEvent = CalEventText(text = name)
            val newEventId = calEventTextQuery.insert(newEvent)
            val newEventPosition = calEventTextQuery.getPositionById(newEventId)
            withContext(Dispatchers.Main) {
                notifyItemInserted(newEventPosition)
            }
        }
    }

    fun deleteAllEvents() {
        lifecycleScope.launch(Dispatchers.IO) {
            calEventTextQuery.deleteAll()
        }
    }
}