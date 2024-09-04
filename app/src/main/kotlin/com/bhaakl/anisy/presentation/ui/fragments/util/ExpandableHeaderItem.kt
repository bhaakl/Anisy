package com.bhaakl.anisy.presentation.ui.fragments.util

import android.graphics.drawable.Animatable
import android.support.annotation.DrawableRes
import com.bhaakl.anisy.R
import com.bhaakl.anisy.databinding.GrpieHeaderItemBinding
import com.bhaakl.anisy.presentation.ui.fragments.util.groupie.item.HeaderItem
import com.bhaakl.anisy.presentation.extensions.toVisible
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem

class ExpandableHeaderItem(
    titleStr: String,
    subtitleStr: String
//    private val pagingDataAdapter: AnimeScheduleAdapter,
//    private val loadStateAdapter: CommonLoadStateAdapter
) : HeaderItem(titleStr, subtitleStr), ExpandableItem {
    @DrawableRes private val expandIcon: Int = R.drawable.ic_down_circle_filled
    @DrawableRes private val collapseIcon: Int = R.drawable.ic_up_circle_filled
    private var expandableGroup: ExpandableGroup? = null

    override fun bind(binding: GrpieHeaderItemBinding, position: Int) {
        super.bind(binding, position)

        binding.grpieIcon.toVisible()
        binding.grpieIcon.setImageResource(if (expandableGroup!!.isExpanded) collapseIcon else expandIcon)
        binding.grpieIcon.setOnClickListener {
            expandableGroup!!.onToggleExpanded()
            bindIcon(binding)
        }
    }

    private fun bindIcon(binding: GrpieHeaderItemBinding) {
        binding.grpieIcon.toVisible()
        binding.grpieIcon.setImageResource(if (expandableGroup!!.isExpanded) collapseIcon else expandIcon)
        (binding.grpieIcon.drawable as? Animatable)?.start()
    }

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        this.expandableGroup = onToggleListener
    }
}
