package com.android.taitasciore.privaliatest.presentation.base

import io.reactivex.Observable

interface BaseView<E : UiEvent, S : ViewState> {

    fun events(): Observable<E>

    fun render(state: S)
}