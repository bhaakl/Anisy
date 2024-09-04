package com.bhaakl.anisy.presentation.model.anime

import android.os.Parcelable
import com.bhaakl.anisy.presentation.base.IBaseDiffModel
import com.bhaakl.anisy.presentation.model.anime.aired.Aired
import com.bhaakl.anisy.presentation.model.anime.genre.Genre
import com.bhaakl.anisy.presentation.model.anime.title.Title
import com.bhaakl.anisy.presentation.model.anime.trailer.Trailer
import kotlinx.parcelize.Parcelize
import com.bhaakl.anisy.presentation.model.anime.image.Images as ImagesUi

@Parcelize
data class AnimeUi(
    override val id: Long,
    val url: String?,
    val images: ImagesUi?,
    val trailer: Trailer?,
    val approved: Boolean?,
    val titles: List<Title>?,
    val type: String?,
    val source: String?,
    val episodes: Int?,
    val status: String?,
    val airing: Boolean?,
    val aired: Aired?,
    val rating: String?,
    val score: Double?,
    val scoredBy: Int?,
    val rank: Int?,
    val popularity: Int?,
    val members: Int?,
    val favorites: Int?,
    val synopsis: String?,
    val background: String?,
    val season: String?,
    val year: Int?,
    val genres: List<Genre>?
) : IBaseDiffModel<Long>, Parcelable {

    /*private val animeMapper: GenericMapper<AnimeUi, Anime>
        get() = Mappers.getMapper(GenericMapper::class.java) as GenericMapper<AnimeUi, Anime>*/
//fun Anime.toUI(): AnimeUi = animeMapper.mapToDomain(this)

}