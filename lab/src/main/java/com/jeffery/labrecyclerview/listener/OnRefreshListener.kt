package com.jeffery.labrecyclerview.listener

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.jeffery.labrecyclerview.LabRecyclerViewAdapter

/**
 *
 * @Date 2017/11/21  11:12
 * @Author Jeffery
 */
abstract class OnRefreshListener : RecyclerView.OnScrollListener() {
    protected var mLastPosition: Int = 0
    protected var mLastPositions: IntArray? = null
    protected var mAdapter: LabRecyclerViewAdapter<*>? = null

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val adapter = recyclerView?.adapter
        if (adapter is LabRecyclerViewAdapter<*>) {
            mAdapter = adapter
            val totalCount = mAdapter?.itemCount
            val visibilityCount = recyclerView.layoutManager?.childCount ?: 0
            if (visibilityCount > 0
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                    && totalCount == mLastPosition + 1) {

                if (!adapter.isLoading()) {
                    addLoadingView(recyclerView)
                    loadMore()
                }
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView?.layoutManager
        when (layoutManager) {
            is LinearLayoutManager -> {
                mLastPosition = layoutManager.findLastVisibleItemPosition()
            }
            is GridLayoutManager -> {
                mLastPosition = layoutManager.findLastVisibleItemPosition()
            }
            is StaggeredGridLayoutManager -> {
                if (mLastPositions == null) {
                    mLastPositions = IntArray(layoutManager.spanCount)
                }
                layoutManager.findLastVisibleItemPositions(mLastPositions)
                mLastPosition = findMax(mLastPositions!!)
            }
        }
    }

    private fun addLoadingView(recyclerView: RecyclerView) {
        mAdapter?.setLoadingView(makeLoadView(recyclerView))
    }

    private fun makeLoadView(view: RecyclerView): View {
        val loadView = LinearLayout(view.context)
        loadView.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
        loadView.orientation = LinearLayout.HORIZONTAL
        val progressBar = ProgressBar(view.context)
        progressBar.layoutParams = LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        loadView.addView(progressBar)
        val textView = TextView(view.context)
        textView.layoutParams = LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT, 2f)
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.text = "正在加载..."
        loadView.addView(textView)
        loadView.setPadding(10, 5, 5, 10)
        return loadView
    }

    private fun findMax(lastPositions: IntArray): Int = lastPositions.max() ?: lastPositions[0]

    abstract fun loadMore()
}