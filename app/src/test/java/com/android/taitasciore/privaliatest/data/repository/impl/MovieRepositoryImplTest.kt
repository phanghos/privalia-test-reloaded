package com.android.taitasciore.privaliatest.data.repository.impl

import com.android.taitasciore.privaliatest.data.entity.MovieEntity
import com.android.taitasciore.privaliatest.data.entity.MovieListResponseEntity
import com.android.taitasciore.privaliatest.data.entity.mapper.MovieMapper
import com.android.taitasciore.privaliatest.data.net.TmdbService
import com.android.taitasciore.privaliatest.data.repository.MovieRepository
import com.android.taitasciore.privaliatest.domain.model.Movie
import com.android.taitasciore.privaliatest.domain.model.MovieListResponse
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.Spy
import java.net.UnknownHostException

class MovieRepositoryImplTest {

    @Mock
    lateinit var tmdbService: TmdbService

    @Spy
    lateinit var movieMapper: MovieMapper

    private lateinit var movieRepository: MovieRepository

    @Before
    fun setUp() {
        initMocks(this)
        movieRepository = MovieRepositoryImpl(tmdbService, movieMapper)
    }

    @Test
    fun `getPopularMovies when request is successful`() {
        // Given
        val movieEntity = MovieEntity()
        val movieListEntity = listOf(movieEntity)
        val movieListResponseEntity = MovieListResponseEntity(results = movieListEntity)
        val movie = Movie()
        val expected = listOf(movie)
        val movieListResponse = MovieListResponse(results = expected)
        `when`(tmdbService.getPopularMovies(1)).thenReturn(Observable.just(movieListResponseEntity))

        // When
        val result = movieRepository.getPopularMovies(1)
        val testObserver = result.test()

        // Then
        verify(tmdbService).getPopularMovies(1)
        verify(movieMapper).map(movieListEntity)
        testObserver.assertSubscribed()
        testObserver.assertValueAt(0, expected)
        testObserver.assertValueCount(1)
        testObserver.assertNoErrors()
    }

    @Test
    fun `getPopularMovies when request fails`() {
        // Given
        val exception = UnknownHostException()
        `when`(tmdbService.getPopularMovies(1)).thenReturn(Observable.error(exception))

        // When
        val result = movieRepository.getPopularMovies(1)
        val testObserver = result.test()

        // Then
        verify(tmdbService).getPopularMovies(1)
        verifyZeroInteractions(movieMapper)
        testObserver.assertSubscribed()
        testObserver.assertValueCount(0)
        testObserver.assertError(exception)
        assertEquals(1, testObserver.errorCount())
    }

    @Test
    fun `searchMovies when request is successful`() {
        // Given
        val movieEntity = MovieEntity()
        val movieListEntity = listOf(movieEntity)
        val movieListResponseEntity = MovieListResponseEntity(results = movieListEntity)
        val movie = Movie()
        val expected = listOf(movie)
        val movieListResponse = MovieListResponse(results = expected)
        `when`(tmdbService.searchMovies("query", 1)).thenReturn(Observable.just(movieListResponseEntity))

        // When
        val result = movieRepository.searchMovies("query", 1)
        val testObserver = result.test()

        // Then
        verify(tmdbService).searchMovies("query", 1)
        verify(movieMapper).map(movieListEntity)
        testObserver.assertSubscribed()
        testObserver.assertValueAt(0, expected)
        testObserver.assertValueCount(1)
        testObserver.assertNoErrors()
    }

    @Test
    fun `searchMovies when request fails`() {
        // Given
        val exception = UnknownHostException()
        `when`(tmdbService.searchMovies("query", 1)).thenReturn(Observable.error(exception))

        // When
        val result = movieRepository.searchMovies("query", 1)
        val testObserver = result.test()

        // Then
        verify(tmdbService).searchMovies("query", 1)
        verifyZeroInteractions(movieMapper)
        testObserver.assertSubscribed()
        testObserver.assertValueCount(0)
        testObserver.assertError(exception)
        assertEquals(1, testObserver.errorCount())
    }
}