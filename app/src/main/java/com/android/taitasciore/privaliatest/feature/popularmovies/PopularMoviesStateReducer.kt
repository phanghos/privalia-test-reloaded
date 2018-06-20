package com.android.taitasciore.privaliatest.feature.popularmovies

import com.android.taitasciore.privaliatest.domain.model.Movie
import com.android.taitasciore.privaliatest.exception.MovieQueryNotFoundException
import com.android.taitasciore.privaliatest.presentation.base.Resource
import io.reactivex.functions.BiFunction
import javax.inject.Inject

/**
 * This is the state reducer that generates a PopularMoviesViewState from a PopularMoviesResult and previous state
 */
open class PopularMoviesStateReducer @Inject constructor() : BiFunction<PopularMoviesViewState, PopularMoviesResult, PopularMoviesViewState> {

    /**
     * @param previousState Previous state
     * @param result New result
     * @return New state built from new result + previous state
     */
    override fun apply(previousState: PopularMoviesViewState, result: PopularMoviesResult): PopularMoviesViewState {
        return when (result.status) {
            Resource.Status.LOADING -> previousState.copy(
                    loading = true
            )
            Resource.Status.SUCCESS -> {
                var movies = ArrayList<Movie>()

                if (result.page == 1) {
                    movies.addAll(result.movies!!)
                } else {
                    movies = mergeLists(previousState.movies, result.movies!!) as ArrayList<Movie>
                }

                previousState.copy(
                        loading = false,
                        movies = movies,
                        error = null,
                        showInitialError = false,
                        showSnackbarError = false
                )
            }
            Resource.Status.ERROR -> previousState.copy(
                    loading = false,
                    movies = previousState.movies,
                    error = result.error,
                    showInitialError = result.page == 1, // Error layout is displayed whenever fetching the first page of results only
                    showSnackbarError = result.page > 1, // Snackbar is shown only on pagination so the list of previously fetched results is still displayed
                    search = result.search
            )
        }
    }

    /**
     * This method merges the list from the previous state with the list contained in the new incoming result
     * from the action processor
     * @param previousList List from previous state
     * @param newMovies List of movies from new result
     * @return Merged list of results from previous and new list
     */
    private fun mergeLists(previousList: List<Movie>?, newMovies: List<Movie>): List<Movie> {
        return if (previousList != null) {
            (previousList as ArrayList).addAll(newMovies)
            previousList
        } else {
            newMovies
        }
    }
}