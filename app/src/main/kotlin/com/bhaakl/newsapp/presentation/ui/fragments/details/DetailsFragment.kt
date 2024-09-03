package com.bhaakl.newsapp.presentation.ui.fragments.details

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bhaakl.newsapp.R
import com.bhaakl.newsapp.databinding.FragmentDetailBinding
import com.bhaakl.newsapp.presentation.base.BaseFragment
import com.bhaakl.newsapp.presentation.model.AnimeUi
import com.bhaakl.newsapp.presentation.model.MediaItem
import com.bhaakl.newsapp.presentation.model.MediaType
import com.bhaakl.newsapp.presentation.ui.fragments.util.vp2_gallery.GalleryPagerAdapter
import com.bhaakl.newsapp.domain.util.SingleEvent
import com.bhaakl.newsapp.domain.util.setupSnackbar
import com.bhaakl.newsapp.domain.util.toGone
import com.bhaakl.newsapp.domain.util.toVisible
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment :
    BaseFragment<DetailsViewModel, FragmentDetailBinding>(R.layout.fragment_detail) {

    override val viewModel: DetailsViewModel by viewModels()
    override val binding by viewBinding(FragmentDetailBinding::bind)
    private val args: DetailsFragmentArgs by navArgs()

    private val medias: ArrayList<MediaItem> = ArrayList()

    override fun initialize() {
        viewModel.initIntentData(args.animeUi)
    }

    override fun setupSubscribers() {
        viewModel.animeData.observeSafely(
            lifecycleOwner = viewLifecycleOwner,
            lifecycleState = Lifecycle.State.STARTED
        ) { initializeView(it) }
        viewModel.galleryImgLoadingState.observeUIState(
            viewLifecycleOwner,
            onError = { (code, msg) ->
                listOf(code, msg).firstNotNullOfOrNull {
                    when (it) {
                        is Int -> viewModel.showSnackBarMessage(null, it)
                        is String -> viewModel.showSnackBarMessage(msg)
                    }
                }
            },
            onSuccess = {
                settingTabsGallery(it)
            }
        )
        observeSnackBarMessages(viewModel.showSnackBar)
    }

    private fun initializeView(animeUi: AnimeUi) = with(binding) {
        tvName.text = animeUi.titles?.fold("") { acc, title ->
            when (title.type) {
                "Default" -> acc + title.title
                "Japanese" -> acc + " (${title.title})"
                else -> acc
            }
        }
        tvSynopsis.text =
            if (!animeUi.synopsis.isNullOrBlank())
                animeUi.synopsis else animeUi.background
        tvHeadline.text = animeUi.titles?.find { it.type == "Synonym" }?.let { it.title + " - medias: " } ?: "Below are the medias for this anime."
        Pair(animeUi.images?.jpg, animeUi.trailer?.youtubeId).let { (jpg, vid) ->
            jpg?.let {
                medias.add(
                    if (it.largeImageUrl.isBlank())
                        MediaItem(it.imageUrl, MediaType.IMAGE)
                    else MediaItem(it.largeImageUrl, MediaType.IMAGE)
                )
            }
            vid?.let { medias.add(MediaItem(it, MediaType.VIDEO)) }
        }
        medias.isNotEmpty().let {
            vp2Gallery.adapter = GalleryPagerAdapter(childFragmentManager, lifecycle, medias)
            viewModel.loadGalleryTabThumbs(medias, animeUi.images?.jpg?.smallImageUrl)
        }
    }

    private fun settingTabsGallery(
        bitmaps: List<Pair<Bitmap, MediaType>>,
    ) = with(binding) {
        TabLayoutMediator(vp2GalleryTabs, vp2Gallery) { tab, position ->
            val customView =
                LayoutInflater.from(context)
                    .inflate(R.layout.custom_tab_layout, vp2GalleryTabs, false)
            val tabIcon = customView.findViewById<ImageView>(R.id.tab_icon)
            val videoIconPholder =
                customView.findViewById<ImageView>(R.id.video_icon_placeholder)
            if (position < bitmaps.size) {
                val (bitmap, type) = bitmaps[position]
                tabIcon.setImageBitmap(bitmap)
                when (type) {
                    MediaType.IMAGE -> videoIconPholder.toGone()
                    MediaType.VIDEO -> {
                        videoIconPholder.imageTintList =
                            ContextCompat.getColorStateList(
                                requireContext(),
                                R.color.white
                            )
                        videoIconPholder.toVisible()
                    }
                }
            }
            tab.customView = customView
        }.attach()
    }

    // util-funs
    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(
            viewLifecycleOwner,
            { },
            event,
            Snackbar.LENGTH_LONG
        )
    }
}
