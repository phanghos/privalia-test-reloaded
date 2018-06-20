package com.android.taitasciore.privaliatest.utils

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.taitasciore.privaliatest.BaseApplication
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment extensions
 */
fun Fragment.getBaseApplication(): BaseApplication {
    return activity?.application as BaseApplication
}

fun Fragment.showView(view: View) {
    view.visibility = View.VISIBLE
}

fun Fragment.hideView(view: View) {
    view.visibility = View.GONE
}

fun Fragment.showSnackbar(view: View, msg: String) {
    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
}

fun String.parseSimpleDate(): Calendar {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    val date = simpleDateFormat.parse(this)
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar
}

/**
 * RecyclerView extensions
 */
fun RecyclerView.addDivider(context: Context?, resId: Int) {
    val divider = ContextCompat.getDrawable(context!!, resId)
    val itemDecoration = DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL)
    divider?.let {
        itemDecoration.setDrawable(divider)
        this.addItemDecoration(itemDecoration)
    }
}
