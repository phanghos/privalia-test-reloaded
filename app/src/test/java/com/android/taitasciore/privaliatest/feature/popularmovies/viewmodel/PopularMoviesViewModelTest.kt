package com.android.taitasciore.privaliatest.feature.popularmovies.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.android.taitasciore.privaliatest.data.repository.MovieRepository
import com.android.taitasciore.privaliatest.domain.model.Movie
import com.android.taitasciore.privaliatest.feature.popularmovies.PopularMoviesActionProcessor
import com.android.taitasciore.privaliatest.feature.popularmovies.PopularMoviesStateReducer
import com.android.taitasciore.privaliatest.feature.popularmovies.PopularMoviesUiEvent
import com.android.taitasciore.privaliatest.feature.popularmovies.PopularMoviesViewState
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.Spy
import java.net.UnknownHostException

class PopularMoviesViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var movieRepository: MovieRepository

    private lateinit var popularMoviesActionProcessor: PopularMoviesActionProcessor

    @Spy
    lateinit var popularMoviesStateReducer: PopularMoviesStateReducer

    private lateinit var viewModel: PopularMoviesViewModel

    @Before
    fun setUp() {
        initMocks(this)
        popularMoviesActionProcessor = spy(PopularMoviesActionProcessor(movieRepository))
        popularMoviesStateReducer = spy(PopularMoviesStateReducer())
        viewModel = PopularMoviesViewModel(popularMoviesActionProcessor, popularMoviesStateReducer)

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `processUiEvents when event is Initial page is 1 yields success`() {
        // Given
        val observer = mock(Observer::class.java)
        val movie = Movie()
        val movieList = listOf(movie)
        val expected = PopularMoviesViewState(
                loading = false,
                movies =  movieList,
                error = null,
                showInitialError = false,
                showSnackbarError = false,
                search = false
        )
        `when`(movieRepository.getPopularMovies(1)).thenReturn(Observable.just(movieList))

        // When
        viewModel.states().observeForever(observer as Observer<PopularMoviesViewState>)
        viewModel.processUiEvents(Observable.just(PopularMoviesUiEvent.Initial()))

        // Then
        verify(movieRepository).getPopularMovies(1)
        verify(observer).onChanged(expected)
        assertEquals(expected, viewModel.states().value)
    }

    @Test
    fun `processUiEvents when event is Initial page is 1 yields error`() {
        // Given
        val observer = mock(Observer::class.java)
        val exception = UnknownHostException()
        val expected = PopularMoviesViewState(
                loading = false,
                movies =  null,
                error = exception,
                showInitialError = true,
                showSnackbarError = false,
                search = false
        )
        `when`(movieRepository.getPopularMovies(1)).thenReturn(Observable.error(exception))

        // When
        viewModel.states().observeForever(observer as Observer<PopularMoviesViewState>)
        viewModel.processUiEvents(Observable.just(PopularMoviesUiEvent.Initial()))

        // Then
        verify(movieRepository).getPopularMovies(1)
        verify(observer).onChanged(expected)
        assertEquals(expected, viewModel.states().value)
    }

    @Test
    fun `processUiEvents when event is Search page is 1 yields success`() {
        // Given
        val observer = mock(Observer::class.java)
        val movie = Movie()
        val movieList = listOf(movie)
        val expected = PopularMoviesViewState(
                loading = false,
                movies =  movieList,
                error = null,
                showInitialError = false,
                showSnackbarError = false,
                search = false
        )
        `when`(movieRepository.searchMovies("query", 1)).thenReturn(Observable.just(movieList))

        // When
        viewModel.states().observeForever(observer as Observer<PopularMoviesViewState>)
        viewModel.processUiEvents(Observable.just(PopularMoviesUiEvent.Search("query")))

        // Then
        verify(movieRepository).searchMovies("query", 1)
        verify(observer).onChanged(expected)
        assertEquals(expected, viewModel.states().value)
    }

    @Test
    fun `processUiEvents when event is Search page is 1 yields error`() {
        // Given
        val observer = mock(Observer::class.java)
        val exception = UnknownHostException()
        val expected = PopularMoviesViewState(
                loading = false,
                movies =  null,
                error = exception,
                showInitialError = true,
                showSnackbarError = false,
                search = true
        )
        `when`(movieRepository.searchMovies("query", 1)).thenReturn(Observable.error(exception))

        // When
        viewModel.states().observeForever(observer as Observer<PopularMoviesViewState>)
        viewModel.processUiEvents(Observable.just(PopularMoviesUiEvent.Search("query")))

        // Then
        verify(movieRepository).searchMovies("query", 1)
        verify(observer).onChanged(expected)
        assertEquals(expected, viewModel.states().value)
    }

    @Test
    fun `processUiEvents when event is LoadNext page is 1 yields success`() {
        // Given
        val observer = mock(Observer::class.java)
        val movie = Movie()
        val movieList = listOf(movie)
        val expected = PopularMoviesViewState(
                loading = false,
                movies =  movieList,
                error = null,
                showInitialError = false,
                showSnackbarError = false,
                search = false
        )
        `when`(movieRepository.getPopularMovies(1)).thenReturn(Observable.just(movieList))

        // When
        viewModel.states().observeForever(observer as Observer<PopularMoviesViewState>)
        viewModel.processUiEvents(Observable.just(PopularMoviesUiEvent.LoadNext()))

        // Then
        verify(movieRepository).getPopularMovies(1)
        verify(observer).onChanged(expected)
        assertEquals(expected, viewModel.states().value)
    }

    @Test
    fun `processUiEvents when event is LoadNext page is 1 yields error`() {
        // Given
        val observer = mock(Observer::class.java)
        val exception = UnknownHostException()
        val expected = PopularMoviesViewState(
                loading = false,
                movies =  null,
                error = exception,
                showInitialError = true,
                showSnackbarError = false,
                search = false
        )
        `when`(movieRepository.getPopularMovies(1)).thenReturn(Observable.error(exception))

        // When
        viewModel.states().observeForever(observer as Observer<PopularMoviesViewState>)
        viewModel.processUiEvents(Observable.just(PopularMoviesUiEvent.LoadNext()))

        // Then
        verify(movieRepository).getPopularMovies(1)
        verify(observer).onChanged(expected)
        assertEquals(expected, viewModel.states().value)
    }
}