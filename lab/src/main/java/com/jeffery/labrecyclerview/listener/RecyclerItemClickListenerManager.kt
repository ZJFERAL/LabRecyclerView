package com.jeffery.labrecyclerview.listener

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 *
 * @Date 2017/11/21  10:46
 * @Author Jeffery
 */
class RecyclerItemClickListenerManager(
        context: Context,
        private val listener: BaseClickListener
) : RecyclerView.SimpleOnItemTouchListener() {

    private var childView: View? = null
    private var touchView: RecyclerView? = null
    private val mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            if (childView != null) {
                if (listener is OnItemSimpleClickListener) {
                    listener.onItemClick(childView!!, touchView!!.getChildAdapterPosition(childView))
                }
            }
            return true
        }

        override fun onLongPress(e: MotionEvent?) {
            if (childView != null) {
                if (listener is OnItemLongClickListener) {
                    listener.onItemLongClickListener(childView!!, touchView!!.getChildAdapterPosition(childView))
                }
            }
        }
    })

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
        if (e != null) {
            mGestureDetector.onTouchEvent(e)
            childView = rv?.findChildViewUnder(e.x, e.y)
            touchView = rv
        }
        return false
    }


}