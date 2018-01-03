package com.tgirard12.krecyclerdsl

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Call the function when scroll down on the last visible item
 *
 * This function could be call several times, so you need to handle a loading status manually
 */
fun RecyclerView.addInfiniteScrollListener(f: () -> Unit): Unit {
    if (this.layoutManager !is LinearLayoutManager)
        throw IllegalArgumentException("layoutManager must be a LinearLayoutManager")

    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            //check for scroll down
            if (dy > 0) {
                val lastVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                val itemCount = layoutManager.itemCount
                if (lastVisibleItemPosition == itemCount - 1)
                    f()
            }
        }
    })
}

/**
 * Add separation [DividerItemDecoration] between each line.
 *
 * This [RecyclerView] must have a [LinearLayoutManager]
 */
fun RecyclerView.addDividerItemDecorationVertical() =
        this.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))

/**
 * Add separation [DividerItemDecoration] between each column
 *
 * This [RecyclerView] must have a [LinearLayoutManager]
 */
fun RecyclerView.addDividerItemDecorationHorizontal() =
        this.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL))
