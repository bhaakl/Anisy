package com.bhaakl.anisy.presentation.ui.fragments.util.groupie.item

import androidx.annotation.DrawableRes
import android.view.View
import com.bhaakl.anisy.R
import com.bhaakl.anisy.databinding.GrpieHeaderItemBinding
import com.xwray.groupie.viewbinding.BindableItem

open class HeaderItem @JvmOverloads constructor(
    private val title: String,
    private val subtitle: String = "",
    @DrawableRes private val iconResId: Int = 0,
    private val onIconClickListener: View.OnClickListener? = null
) : BindableItem<GrpieHeaderItemBinding?>() {

    override fun getLayout(): Int = R.layout.grpie_header_item

    override fun initializeViewBinding(view: View): GrpieHeaderItemBinding = GrpieHeaderItemBinding.bind(view)

    override fun bind(binding: GrpieHeaderItemBinding, position: Int) {
        binding.apply {
            tvGrpieTitle.text = title
            tvGrpieSubtitle.text = subtitle
            tvGrpieSubtitle.visibility = if (subtitle.isNotEmpty()) View.VISIBLE else View.GONE

            if (iconResId != 0) {
                grpieIcon.setImageResource(iconResId)
                grpieIcon.setOnClickListener(onIconClickListener)
            }
            grpieIcon.visibility = if (iconResId != 0) View.VISIBLE else View.GONE
        }
    }
}