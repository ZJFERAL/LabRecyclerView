package com.jeffery.labrecyclerview.listener

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.jeffery.labrecyclerview.LabRecyclerViewAdapter

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
                val position = touchView!!.getChildAdapterPosition(childView)
                val adapter = touchView!!.adapter
                if (adapter is LabRecyclerViewAdapter<*>) {
                    if (listener is OnItemSimpleClickListener && (
                            !adapter.needShowLoadingView(position)
                                    || !adapter.needShowHeaderView(position)
                                    || !adapter.needShowFooterView(position)
                            )) {
                        listener.onItemClick(childView!!, position)
                    }
                }
            }
            return true
        }

        override fun onLongPress(e: MotionEvent?) {
            if (childView != null) {
                val position = touchView!!.getChildAdapterPosition(childView)
                val adapter = touchView!!.adapter
                if (adapter is LabRecyclerViewAdapter<*>) {
                    if (listener is OnItemLongClickListener && (
                            !adapter.needShowLoadingView(position)
                                    || !adapter.needShowHeaderView(position)
                                    || !adapter.needShowFooterView(position)
                            )) {
                        listener.onItemLongClickListener(childView!!, touchView!!.getChildAdapterPosition(childView))
                    }
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