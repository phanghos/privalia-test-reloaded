package com.android.taitasciore.privaliatest.feature.popularmovies.viewmodel

import com.android.taitasciore.privaliatest.feature.popularmovies.*
import com.android.taitasciore.privaliatest.presentation.base.BaseViewModelImpl
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

/**
 * ViewModel for PopularMoviesFragment that receives PopularMoviesUiEvent's,
 * maps them to a PopularMoviesAction to be processed by the action processor,
 * which will produce a PopularMoviesResult, which will be then passed to the
 * state reducer, which will build a PopularMoviesViewState from the previous state
 * + new result :)
 */
class PopularMoviesViewModel @Inject constructor(
        private val popularMoviesActionProcessor: PopularMoviesActionProcessor,
        private val popularMoviesStateReducer: PopularMoviesStateReducer)
    : BaseViewModelImpl<PopularMoviesUiEvent, PopularMoviesViewState>() {

    /**
     * This method takes care of filtering certain UiEvents, such as the Initial event,
     * ensuring that it's handled once and once only. Further incoming Initial events
     * will be filtered out
     */
    private val eventFilter = ObservableTransformer<PopularMoviesUiEvent, PopularMoviesUiEvent> { event ->
        event.publish { shared ->
            Observable.merge(
                    shared.ofType(PopularMoviesUiEvent.Initial::class.java).take(1), // Take the first one
                    shared.filter { it !is PopularMoviesUiEvent.Initial } // Ignore if it' an Initial event
            )
        }
    }

    init {
        statesObservable = compose()
        addDisposable(statesObservable.subscribe { statesLiveData.value = it })
    }

    /**
     * This mehod maps a PopularMoviesUiEvent to its corresponding PopularMoviesAction
     * @param event UiEvent to be mapped to Action
     * @return Mapped Action from UiEvent
     */
    private fun actionFromEvent(event: PopularMoviesUiEvent): PopularMoviesAction {
        return when (event) {
            is PopularMoviesUiEvent.Initial -> PopularMoviesAction.LoadFirstPage()
            is PopularMoviesUiEvent.LoadFirst -> PopularMoviesAction.LoadFirstPage()
            is PopularMoviesUiEvent.LoadNext -> PopularMoviesAction.LoadNextPage()
            is PopularMoviesUiEvent.Retry -> PopularMoviesAction.LoadFirstPage()
            is PopularMoviesUiEvent.RetrySearch -> PopularMoviesAction.SearchMovies(event.query)
            is PopularMoviesUiEvent.Search -> PopularMoviesAction.SearchMovies(event.query)
            is PopularMoviesUiEvent.LoadNextSearch -> PopularMoviesAction.LoadNextSearchPage(event.query)
        }
    }

    /**
     * This is where the magic happens
     */
    override fun compose(): Observable<PopularMoviesViewState> {
        return eventsSubject
                .compose(eventFilter) // Filter UiEvent's
                .map { actionFromEvent(it) } // Map UiEvent to Action
                .compose(popularMoviesActionProcessor.resultFromAction()) // Process action and generate PopularMoviesResult
                .scan(PopularMoviesViewState(false, null, null), popularMoviesStateReducer) // Reduce PopularMoviesResult to PopularMoviesViewState
                .replay(1)
                .autoConnect(0)
    }

    /**
     * This method emits an item to the UiEvents PublishSubject to be processed
     * @param event UiEvent to be processed
     */
    fun sendEvent(event: PopularMoviesUiEvent) {
        eventsSubject.onNext(event)
    }
}