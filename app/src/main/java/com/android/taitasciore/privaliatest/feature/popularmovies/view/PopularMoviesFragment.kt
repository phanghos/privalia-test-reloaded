package com.android.taitasciore.privaliatest.feature.popularmovies.view

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.android.taitasciore.privaliatest.R
import com.android.taitasciore.privaliatest.domain.model.Movie
import com.android.taitasciore.privaliatest.exception.MovieQueryNotFoundException
import com.android.taitasciore.privaliatest.exception.NoMorePagesException
import com.android.taitasciore.privaliatest.feature.popularmovies.PopularMoviesUiEvent
import com.android.taitasciore.privaliatest.feature.popularmovies.PopularMoviesViewState
import com.android.taitasciore.privaliatest.feature.popularmovies.view.adapter.MovieAdapter
import com.android.taitasciore.privaliatest.feature.popularmovies.viewmodel.PopularMoviesViewModel
import com.android.taitasciore.privaliatest.feature.popularmovies.viewmodel.PopularMoviesViewModelFactory
import com.android.taitasciore.privaliatest.presentation.base.BaseView
import com.android.taitasciore.privaliatest.utils.*
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_popular_movies.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PopularMoviesFragment : Fragment(), BaseView<PopularMoviesUiEvent, PopularMoviesViewState> {

    private var query: String = ""

    @Inject
    lateinit var factory: PopularMoviesViewModelFactory

    private val viewModel: PopularMoviesViewModel by lazy {
        ViewModelProviders.of(this, factory)[PopularMoviesViewModel::class.java]
    }

    private val movieAdapter = MovieAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_popular_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseApplication().component.inject(this)
        setHasOptionsMenu(true)
        setupRecyclerView()

        viewModel.states().observe(this, Observer { render(it!!) })
        viewModel.processUiEvents(events())
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Inflate the options menu from XML
        inflater!!.inflate(R.menu.search, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu!!.findItem(R.id.search).actionView as SearchView
        searchView.setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        searchView.queryHint = getString(R.string.search_hint)

        setSearchViewAction(searchView)
    }

    private fun setSearchViewAction(searchView: SearchView) {
        RxSearchView.queryTextChanges(searchView)
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter { it.isEmpty() || it.length > 3 }
                .doOnNext { query = it.toString() }
                .subscribe {
                    if (it.isEmpty()) viewModel.sendEvent(PopularMoviesUiEvent.LoadFirst())
                    else viewModel.sendEvent(PopularMoviesUiEvent.Search(it.toString()))
                }
    }

    private fun setupRecyclerView() {
        list.addDivider(context!!, R.drawable.divider)
        list.adapter = movieAdapter
    }

    private fun initialEvent() = Observable.just(PopularMoviesUiEvent.Initial())

    private fun loadNextPageEvent(): Observable<PopularMoviesUiEvent> {
        return RxRecyclerView.scrollEvents(list).filter {
            val layoutManager = list.layoutManager as LinearLayoutManager
            layoutManager.findLastCompletelyVisibleItemPosition() >= layoutManager.itemCount - 1
        }
                .throttleFirst(1, TimeUnit.SECONDS)
                .map {
                    if (query.isEmpty())PopularMoviesUiEvent.LoadNext()
                    else PopularMoviesUiEvent.LoadNextSearch(query)
                }
    }

    private fun retryEvent(): Observable<PopularMoviesUiEvent> {
        return RxView.clicks(button_retry).map {
            if (query.isEmpty()) PopularMoviesUiEvent.Retry()
            else PopularMoviesUiEvent.RetrySearch(query)
        }
    }

    override fun events(): Observable<PopularMoviesUiEvent> {
        return Observable.merge(initialEvent(), loadNextPageEvent(), retryEvent())
    }

    override fun render(state: PopularMoviesViewState) {
        if (state.loading) {
            renderLoadingState()
        } else {
            if (state.movies != null) renderMovies(state.movies)
            if (state.error != null) renderError(state)
        }
    }

    private fun renderLoadingState() {
        showView(progress_wheel)
        hideView(layout_error)
    }

    private fun renderMovies(movies: List<Movie>) {
        hideView(progress_wheel)
        hideView(layout_error)
        showView(list)
        movieAdapter.setList(movies)
    }

    private fun renderError(state: PopularMoviesViewState) {
        hideView(progress_wheel)

        if (state.showInitialError!!) {
            showView(layout_error)
            hideView(list)

            if (state.error is MovieQueryNotFoundException) {
                hideView(button_retry)
                text_view_error.text = getString(R.string.error_message_not_found)
            } else {
                showView(button_retry)
                text_view_error.text = getString(R.string.error_message)
            }
        } else {
            hideView(layout_error)
            showView(list)
        }

        if (state.showSnackbarError!!) {
            val msg: String = if (state.error is NoMorePagesException) state.error!!.message!! else getString(R.string.error_message)
            showSnackbar(activity?.findViewById(R.id.root_layout)!!, msg)
        }
    }
}