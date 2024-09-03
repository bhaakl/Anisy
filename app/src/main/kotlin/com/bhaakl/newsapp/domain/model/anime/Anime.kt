package com.bhaakl.newsapp.domain.model.anime

import com.bhaakl.newsapp.data.remote.dto.anime.aired.Aired
import com.bhaakl.newsapp.data.remote.dto.anime.genre.Genre
import com.bhaakl.newsapp.data.remote.dto.anime.image.Images
import com.bhaakl.newsapp.data.remote.dto.anime.title.Title
import com.bhaakl.newsapp.data.remote.dto.anime.trailer.Trailer
import kotlinx.serialization.Serializable

@Serializable
data class Anime(
    val malId: Int?,
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
) {
    /*private val animeMapper: GenericMapper<*, *>
        get() = Mappers.getMapper(DomainMapper::class.java) as GenericMapper<*, *>

    fun mapToDto() = animeMapper.mapToDto(this)*/
}