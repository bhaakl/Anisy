package com.bhaakl.newsapp.data.remote.dto.anime

import android.os.Parcelable
import com.bhaakl.newsapp.data.remote.dto.anime.aired.Aired
import com.bhaakl.newsapp.data.remote.dto.anime.genre.Genre
import com.bhaakl.newsapp.data.remote.dto.anime.image.Images
import com.bhaakl.newsapp.data.remote.dto.anime.title.Title
import com.bhaakl.newsapp.data.remote.dto.anime.trailer.Trailer
import com.bhaakl.newsapp.domain.model.anime.Anime
import com.bhaakl.newsapp.data.util.mapper.DataMapper
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeDto(
    @SerialName("mal_id") val malId: Int? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("images") val images: Images? = null,
    @SerialName("trailer") val trailer: Trailer? = null,
    @SerialName("approved") val approved: Boolean? = null,
    @SerialName("titles") val titles: List<Title>? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("source") val source: String? = null,
    @SerialName("episodes") val episodes: Int? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("airing") val airing: Boolean? = null,
    @SerialName("aired") val aired: Aired? = null,
    @SerialName("rating") val rating: String? = null,
    @SerialName("score") val score: Double? = null,
    @SerialName("scored_by") val scoredBy: Int? = null,
    @SerialName("rank") val rank: Int? = null,
    @SerialName("popularity") val popularity: Int? = null,
    @SerialName("members") val members: Int? = null,
    @SerialName("favorites") val favorites: Int? = null,
    @SerialName("synopsis") val synopsis: String? = null,
    @SerialName("background") val background: String? = null,
    @SerialName("season") val season: String? = null,
    @SerialName("year") val year: Int? = null,
    @SerialName("genres") val genres: List<Genre>? = null
) : DataMapper<Anime> {
    /*private val animeMapper: GenericMapper<Anime, AnimeDto>
        get() = Mappers.getMapper(GenericMapper::class.java) as GenericMapper<Anime, AnimeDto>
*/
    override fun mapToDomain() = Anime(
        malId = malId,
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
}