package com.bhaakl.newsapp.presentation.model

import android.os.Parcelable
import com.bhaakl.newsapp.data.remote.dto.anime.aired.Aired
import com.bhaakl.newsapp.data.remote.dto.anime.genre.Genre
import com.bhaakl.newsapp.data.remote.dto.anime.image.Images
import com.bhaakl.newsapp.data.remote.dto.anime.title.Title
import com.bhaakl.newsapp.data.remote.dto.anime.trailer.Trailer
import com.bhaakl.newsapp.domain.model.anime.Anime
import com.bhaakl.newsapp.presentation.base.IBaseDiffModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnimeUi(
    override val id: Long,
    val url: String?,
    val images: Images?,
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
): IBaseDiffModel<Long>, Parcelable

/*private val animeMapper: GenericMapper<AnimeUi, Anime>
    get() = Mappers.getMapper(GenericMapper::class.java) as GenericMapper<AnimeUi, Anime>*/
//fun Anime.toUI(): AnimeUi = animeMapper.mapToDomain(this)
fun Anime.toUI(): AnimeUi = AnimeUi(
    id = malId?.toLong() ?: 0,
    url = url,
    images = images,
    trailer = trailer,
    approved = approved,
    titles = titles,
    type = type,
    source = source,
    episodes = episodes,
    status = status,
    airing = airing,
    aired = aired,
    rating = rating,
    score = score,
    scoredBy = scoredBy,
    rank = rank,
    popularity = popularity,
    members = members,
    favorites = favorites,
    synopsis = synopsis,
    background = background,
    season = season,
    year = year,
    genres = genres,
)