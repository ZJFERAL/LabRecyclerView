package com.jeffery.labrecyclerview.listener

import android.view.View

/**
 *
 * @Date 2017/11/21  10:43
 * @Author Jeffery
 */
interface OnItemLongClickListener:BaseClickListener {
    fun onItemLongClickListener(view: View, position: Int)
}