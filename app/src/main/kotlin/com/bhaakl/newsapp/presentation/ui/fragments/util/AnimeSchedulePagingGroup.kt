package com.bhaakl.newsapp.presentation.ui.fragments.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bhaakl.newsapp.R
import com.bhaakl.newsapp.presentation.ui.fragments.main.home.adapter.paging.CommonLoadStateAdapter
import com.bhaakl.newsapp.presentation.ui.fragments.main.schedule.adapter.AnimeScheduleAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class AnimeSchedulePagingGroup(
    private val pagingDataAdapter: AnimeScheduleAdapter,
    private val loadStateAdapter: CommonLoadStateAdapter
) : Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position:Int) {
        val recyclerView = viewHolder.itemView.findViewById<RecyclerView>(R.id.rv_schedule_list)
        recyclerView.layoutManager = LinearLayoutManager(viewHolder.itemView.context)
        recyclerView.adapter = pagingDataAdapter
        recyclerView.adapter = pagingDataAdapter
            .withLoadStateFooter(
                footer = loadStateAdapter
            )
    }

    override fun getLayout(): Int = R.layout.fragment_schedule
}