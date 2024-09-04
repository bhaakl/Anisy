package com.bhaakl.anisy.presentation.ui.fragments.util.vp2_gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bhaakl.anisy.domain.MEDIA_ITEM_KEY
import com.bhaakl.anisy.domain.MEDIA_TYPE_KEY
import com.bhaakl.anisy.R
import com.bhaakl.anisy.presentation.extensions.parcelable
import com.bhaakl.anisy.presentation.model.MediaType
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.squareup.picasso.Picasso

class GalleryFragment : Fragment() {
    private lateinit var youtubePlayerView: YouTubePlayerView
    private lateinit var imageView: ImageView
    private var mediaUrl: String? = null
    private var mediaType: MediaType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mediaUrl = it.getString(MEDIA_ITEM_KEY)
            mediaType = it.parcelable(MEDIA_TYPE_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.gallery_img_item, container, false)
        youtubePlayerView = view.findViewById(R.id.ypv_anime_video)
        imageView = view.findViewById(R.id.iv_anime_image)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(youtubePlayerView)

        when (mediaType) {
            MediaType.IMAGE -> {
                youtubePlayerView.visibility = View.GONE
                imageView.visibility = View.VISIBLE
                mediaUrl?.let {
                    Picasso.get().load(it)
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.not_available)
                        .error(R.drawable.not_available)
                        .into(imageView)
                }
            }

            MediaType.VIDEO -> {
                imageView.visibility = View.GONE
                youtubePlayerView.visibility = View.VISIBLE
                mediaUrl?.let { videoId ->
                    youtubePlayerView.addYouTubePlayerListener(object :
                        AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.loadVideo(videoId, 0f)
                        }
                    })
                }
            }

            else -> throw IllegalStateException("Unknown media type: $mediaType")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(youtubePlayerView)
    }

    companion object {
        fun newInstance(mediaUrl: String, mediaType: MediaType) =
            GalleryFragment().apply {
                arguments = Bundle().apply {
                    putString(MEDIA_ITEM_KEY, mediaUrl)
                    putSerializable(MEDIA_TYPE_KEY, mediaType)
                }
            }
    }

}