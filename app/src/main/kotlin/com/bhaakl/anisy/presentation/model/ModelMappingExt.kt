package com.bhaakl.anisy.presentation.model

import com.bhaakl.anisy.domain.model.anime.Anime
import com.bhaakl.anisy.domain.model.anime.image.Images
import com.bhaakl.anisy.presentation.model.anime.AnimeUi
import com.bhaakl.anisy.presentation.model.anime.aired.Aired
import com.bhaakl.anisy.presentation.model.anime.date_prop.DateProp
import com.bhaakl.anisy.presentation.model.anime.date_prop.Prop
import com.bhaakl.anisy.presentation.model.anime.genre.Genre
import com.bhaakl.anisy.presentation.model.anime.image.ImageDetails
import com.bhaakl.anisy.presentation.model.anime.title.Title
import com.bhaakl.anisy.presentation.model.anime.trailer.Trailer


fun Anime.toUI(): AnimeUi = AnimeUi(
    id = malId?.toLong() ?: 0,
    url = this.url,
    images = this.images?.toImagesUi(),
    trailer = this.trailer?.toTrailerUi(),
    approved = this.approved,
    titles = this.titles?.map { it.toTitleUi() },
    type = this.type,
    source = this.source,
    episodes = this.episodes,
    status = this.status,
    airing = this.airing,
    aired = this.aired?.toAiredUi(),
    rating = this.rating,
    score = this.score,
    scoredBy = this.scoredBy,
    rank = this.rank,
    popularity = this.popularity,
    members = this.members,
    favorites = this.favorites,
    synopsis = this.synopsis,
    background = this.background,
    season = this.season,
    year = this.year,
    genres = this.genres?.map { it.toGenreUi() }
)

private fun Images.toImagesUi(): com.bhaakl.anisy.presentation.model.anime.image.Images {
    return com.bhaakl.anisy.presentation.model.anime.image.Images(
        jpg = this.jpg.toImageDetailsUi(),
    )
}

private fun com.bhaakl.anisy.domain.model.anime.image.ImageDetails.toImageDetailsUi(): ImageDetails {
    return ImageDetails(
        imageUrl = this.imageUrl,
        smallImageUrl = this.smallImageUrl,
        largeImageUrl = this.largeImageUrl
    )
}

private fun com.bhaakl.anisy.domain.model.anime.trailer.Trailer.toTrailerUi(): Trailer {
    return Trailer(
        youtubeId = this.youtubeId,
        url = this.url,
        embedUrl = this.embedUrl
    )
}

private fun com.bhaakl.anisy.domain.model.anime.aired.Aired.toAiredUi(): Aired {
    return Aired(
        from = this.from,
        to = this.to,
        prop = this.prop?.toPropUi(),
        string = this.string
    )
}

private fun com.bhaakl.anisy.domain.model.anime.date_prop.Prop.toPropUi(): Prop {
    return Prop(
        from = this.from?.toDatePropUi(),
        to = this.to?.toDatePropUi()
    )
}

private fun com.bhaakl.anisy.domain.model.anime.date_prop.DateProp.toDatePropUi(): DateProp {
    return DateProp(
        day = this.day,
        month = this.month,
        year = this.year
    )
}

private fun com.bhaakl.anisy.domain.model.anime.title.Title.toTitleUi() = Title(
    type = this.type,
    title = this.title
)

private fun com.bhaakl.anisy.domain.model.anime.genre.Genre.toGenreUi() = Genre(
    malId = this.malId,
    type = this.type,
    name = this.name,
    url = this.url
)