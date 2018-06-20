package com.android.taitasciore.privaliatest.feature.popularmovies

import com.android.taitasciore.privaliatest.presentation.base.Action

sealed class PopularMoviesAction : Action {

    class LoadFirstPage : PopularMoviesAction()

    class LoadNextPage : PopularMoviesAction()

    data class SearchMovies(val query: String) : PopularMoviesAction()

    data class LoadNextSearchPage(val query: String) : PopularMoviesAction()
}