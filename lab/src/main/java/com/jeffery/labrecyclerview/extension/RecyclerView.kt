package com.jeffery.labrecyclerview.extension

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.jeffery.labrecyclerview.LabRecyclerViewAdapter
import com.jeffery.labrecyclerview.listener.*

/**
 *
 * @Date 2017/11/20  14:47
 * @Author Jeffery
 */
fun RecyclerView.addItemSimpleClickListener(context: Context, listener: OnItemSimpleClickListener) {
    addOnItemTouchListener(RecyclerItemClickListenerManager(context, listener))
}

fun RecyclerView.addItemLongClickListener(context: Context, listener: OnItemLongClickListener) {
    addOnItemTouchListener(RecyclerItemClickListenerManager(context, listener))
}

fun RecyclerView.addItemClickListener(context: Context, listener: OnItemClickListener) {
    addOnItemTouchListener(RecyclerItemClickListenerManager(context, listener))
}

fun RecyclerView.isLoading(): Boolean {
    if (adapter is LabRecyclerViewAdapter<*>) {
        val localAdapter: LabRecyclerViewAdapter<*> = adapter as LabRecyclerViewAdapter<*>
        return localAdapter.isLoading()
    }
    return false
}

fun RecyclerView.completeLoading() {
    if (adapter is LabRecyclerViewAdapter<*>) {
        val localAdapter: LabRecyclerViewAdapter<*> = adapter as LabRecyclerViewAdapter<*>
        localAdapter.removeLoadingView()
    }
}

fun RecyclerView.setOnRefreshListener(listener: OnRefreshListener) {
    addOnScrollListener(listener)
}