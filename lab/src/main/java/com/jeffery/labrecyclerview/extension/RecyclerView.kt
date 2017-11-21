package com.jeffery.labrecyclerview.extension

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.jeffery.labrecyclerview.listener.OnItemClickListener
import com.jeffery.labrecyclerview.listener.OnItemLongClickListener
import com.jeffery.labrecyclerview.listener.OnItemSimpleClickListener
import com.jeffery.labrecyclerview.listener.RecyclerItemClickListenerManager

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