package com.android.taitasciore.privaliatest.feature.popularmovies

import com.android.taitasciore.privaliatest.data.repository.MovieRepository
import com.android.taitasciore.privaliatest.domain.model.Movie
import com.android.taitasciore.privaliatest.exception.MovieQueryNotFoundException
import com.android.taitasciore.privaliatest.exception.NoMorePagesException
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * This class is an action processor a.k.a. a class that processes, in this case, PopularMoviesAction's
 * and generates the corresponding PopularMoviesResult instance
 */
open class PopularMoviesActionProcessor @Inject constructor(private val movieRepository: MovieRepository) {

    private var page: Int = 1 // Current page

    private var pageSearch: Int = 1 // Current page in search

    private val firstPage = ArrayList<Movie>() // Cached first page of results

    /**
     * This method handles LoadFirstPage actions
     */
    private fun loadFirstPage(): ObservableTransformer<PopularMoviesAction.LoadFirstPage, PopularMoviesResult> {
        return ObservableTransformer { action ->
            action.doOnNext { page = 1 }
                    .flatMap {
                        if (!firstPage.isEmpty()) {
                            Observable.just(PopularMoviesResult.success(firstPage, 1))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                        } else {
                            movieRepository.getPopularMovies(1)
                                    .map {
                                        if (!it.isEmpty()) {
                                            firstPage.addAll(it)
                                            page++
                                            PopularMoviesResult.success(it, 1)
                                        } else {
                                            PopularMoviesResult.error(NoMorePagesException())
                                        }
                                    }
                                    .onErrorReturn { PopularMoviesResult.error(it) }
                                    .startWith(PopularMoviesResult.loading())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                        }
            }
        }
    }

    /**
     * This method handles LoadNextPage actions
     */
    private fun loadNextPage(): ObservableTransformer<PopularMoviesAction.LoadNextPage, PopularMoviesResult> {
        return ObservableTransformer { action ->
            action.flatMap {
                movieRepository.getPopularMovies(page)
                        .map {
                            if (!it.isEmpty()) {
                                page++
                                PopularMoviesResult.success(it, page - 1)
                            } else {
                                PopularMoviesResult.error(NoMorePagesException())
                            }
                        }
                        .onErrorReturn { PopularMoviesResult.error(it) }
                        .startWith(PopularMoviesResult.loading())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }

    /**
     * This method handles SearchMovies actions
     */
    private fun searchMovies(): ObservableTransformer<PopularMoviesAction.SearchMovies, PopularMoviesResult> {
        return ObservableTransformer { action ->
            action.doOnNext { pageSearch = 1 }
                    .flatMap {
                movieRepository.searchMovies(it.query, 1)
                        .map {
                            if (!it.isEmpty()) {
                                pageSearch++
                                PopularMoviesResult.success(it, 1)
                            } else {
                                PopularMoviesResult.error(MovieQueryNotFoundException(), true)
                            }
                        }
                        .onErrorReturn { PopularMoviesResult.error(it, true) }
                        .startWith(PopularMoviesResult.loading())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }

    /**
     * This method handles LoadNextSearch actions
     */
    private fun loadNextSearchPage(): ObservableTransformer<PopularMoviesAction.LoadNextSearchPage, PopularMoviesResult> {
        return ObservableTransformer { action ->
            action.flatMap {
                movieRepository.searchMovies(it.query, pageSearch)
                        .map {
                            if (!it.isEmpty()) {
                                pageSearch++
                                PopularMoviesResult.success(it, pageSearch - 1)
                            } else {
                                PopularMoviesResult.error(NoMorePagesException(), true)
                            }
                        }
                        .onErrorReturn { PopularMoviesResult.error(it, true) }
                        .startWith(PopularMoviesResult.loading())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }

    fun resultFromAction(): ObservableTransformer<PopularMoviesAction, PopularMoviesResult> {
        return ObservableTransformer { action ->
            action.publish { shared ->
                Observable.merge(
                        shared.ofType(PopularMoviesAction.LoadFirstPage::class.java).compose(loadFirstPage()),
                        shared.ofType(PopularMoviesAction.LoadNextPage::class.java).compose(loadNextPage()),
                        shared.ofType(PopularMoviesAction.SearchMovies::class.java).compose(searchMovies()),
                        shared.ofType(PopularMoviesAction.LoadNextSearchPage::class.java).compose(loadNextSearchPage())
                )
            }
        }
    }
}