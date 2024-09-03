package com.bhaakl.newsapp.presentation.ui.fragments.util.vp2_gallery;

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bhaakl.newsapp.presentation.model.MediaItem

class GalleryPagerAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle,
    private val mediaItems: List<MediaItem>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = mediaItems.size

    override fun createFragment(position: Int): Fragment {
        val mediaItem = mediaItems[position]
        return GalleryFragment.newInstance(mediaItem.url, mediaItem.type)
    }
}