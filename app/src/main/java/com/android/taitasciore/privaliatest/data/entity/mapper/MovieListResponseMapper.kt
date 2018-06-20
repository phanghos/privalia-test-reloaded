package com.android.taitasciore.privaliatest.data.entity.mapper

import com.android.taitasciore.privaliatest.data.entity.MovieListResponseEntity
import com.android.taitasciore.privaliatest.domain.model.MovieListResponse
import javax.inject.Inject

class MovieListResponseMapper @Inject constructor(private val movieMapper: MovieMapper) : Mapper<MovieListResponseEntity, MovieListResponse> {
    override fun map(input: MovieListResponseEntity): MovieListResponse {
        return MovieListResponse(
                page = input.page,
                totalPages = input.totalPages,
                results = movieMapper.map(input.results!!),
                totalResults = input.totalResults
        )
    }
}