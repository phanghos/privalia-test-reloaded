package com.android.taitasciore.privaliatest.data.entity.mapper

import com.android.taitasciore.privaliatest.data.entity.MovieEntity
import com.android.taitasciore.privaliatest.domain.model.Movie
import javax.inject.Inject

open class MovieMapper @Inject constructor() : Mapper<List<MovieEntity>, List<Movie>> {

    override fun map(input: List<MovieEntity>): List<Movie> {
        return input.map { map(it) }
    }

    private fun map(movieEntity: MovieEntity): Movie {
        return Movie(
                overview = movieEntity.overview,
                originalLanguage = movieEntity.originalLanguage,
                originalTitle = movieEntity.originalTitle,
                title = movieEntity.title,
                posterPath = movieEntity.posterPath,
                backdropPath = movieEntity.backdropPath,
                releaseDate = movieEntity.releaseDate,
                id = movieEntity.id
        )
    }
}