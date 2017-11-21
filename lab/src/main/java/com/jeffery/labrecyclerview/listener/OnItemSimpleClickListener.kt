package com.jeffery.labrecyclerview.listener

import android.view.View

/**
 *
 * @Date 2017/11/21  10:42
 * @Author Jeffery
 */
interface OnItemSimpleClickListener : BaseClickListener {
    fun onItemClick(view: View, position: Int)
}