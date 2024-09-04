package com.bhaakl.anisy.presentation.ui.fragments.util.groupie.item

import android.view.View
import androidx.annotation.ColorInt
import com.bhaakl.anisy.R
import com.bhaakl.anisy.databinding.GrpieItemCardBinding
import com.xwray.groupie.viewbinding.BindableItem

open class CardItem @JvmOverloads constructor(
    @ColorInt private val colorInt: Int,
    val text: CharSequence = ""
) : BindableItem<GrpieItemCardBinding>() {

    /*init {
        extras[INSET_TYPE_KEY] = INSET
    }*/

    override fun getLayout(): Int = R.layout.grpie_item_card

    override fun initializeViewBinding(view: View): GrpieItemCardBinding = GrpieItemCardBinding.bind(view)

    override fun bind(viewBinding: GrpieItemCardBinding, position: Int) {
        viewBinding.root.setBackgroundColor(colorInt)
        viewBinding.text.text = text
    }
}