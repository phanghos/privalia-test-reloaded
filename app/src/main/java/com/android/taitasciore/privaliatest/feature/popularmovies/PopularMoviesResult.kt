package com.android.taitasciore.privaliatest.feature.popularmovies

import com.android.taitasciore.privaliatest.domain.model.Movie
import com.android.taitasciore.privaliatest.presentation.base.Resource
import com.android.taitasciore.privaliatest.presentation.base.ViewState

data class PopularMoviesResult(
        val status: Resource.Status,
        val movies: List<Movie>?,
        val error: Throwable?,
        val page: Int,
        val search: Boolean
) : ViewState {

    companion object {

        fun loading() = PopularMoviesResult(Resource.Status.LOADING, null, null,  1, false)

        fun success(movies: List<Movie>, page: Int) = PopularMoviesResult(Resource.Status.SUCCESS, movies, null, page, false)

        fun error(error: Throwable, search: Boolean = false) = PopularMoviesResult(Resource.Status.ERROR, null, error, 1, search)
    }
}