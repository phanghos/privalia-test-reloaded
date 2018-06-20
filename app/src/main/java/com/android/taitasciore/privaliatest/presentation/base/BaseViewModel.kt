package com.android.taitasciore.privaliatest.presentation.base

import android.arch.lifecycle.LiveData
import io.reactivex.Observable

/**
 * Base interface which every ViewModel following the MVI architecture must implement
 */
interface BaseViewModel<E : UiEvent, S : ViewState> {

    fun processUiEvents(events: Observable<E>)

    fun states(): LiveData<S>
}