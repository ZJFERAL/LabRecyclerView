package com.jeffery.labrecyclerview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 *
 * @Date 2017/11/20  14:18
 * @Author Jeffery
 */
abstract class LabRecyclerViewAdapter<T>(
        protected val mContext: Context,
        protected val mData: List<T>,
        vararg layoutIds: Int
) : RecyclerView.Adapter<LabRecyclerViewViewHolder>() {
    private val mLayoutIds:IntArray = layoutIds

    companion object {
        private val VIEW_TYPE_HEADER: Int = 0x10000001
        private val VIEW_TYPE_FOOTER: Int = 0x10000002
        private val VIEW_TYPE_LOADING: Int = 0x10000003
    }

    private var mHeaderView: View? = null
        set(value) {
            notifyDataSetChanged()
        }
    private var mFooterView: View? = null
    private var mLoadingView: View? = null
    private var mEmptyView: View? = null


    override fun getItemCount(): Int {
        setEmptyViewState()
        return mData.size
    }

    private fun setEmptyViewState() {

    }

    fun getData(): List<T> = mData

    override fun onBindViewHolder(holder: LabRecyclerViewViewHolder?, position: Int) {
        if (holder != null) {
            setConvertView(holder, mData[position], position)
        } else {
            throw Exception("Holder is null")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LabRecyclerViewViewHolder? {
        var holder: LabRecyclerViewViewHolder? = null
        var itemView: View? = null
        when (viewType) {
            VIEW_TYPE_HEADER -> {
                itemView = mHeaderView
            }
            VIEW_TYPE_FOOTER -> {
                itemView = mFooterView
            }
            VIEW_TYPE_LOADING -> {
                itemView = mLoadingView
            }
            else -> {
                holder = LabRecyclerViewViewHolder.Companion.createViewHolder(mContext, parent!!, mLayoutIds[viewType], viewType)
            }
        }


        if (itemView != null) {// not normal view

            holder = LabRecyclerViewViewHolder.createViewHolder(mContext, itemView, viewType)
        }

        return holder
    }

    abstract fun setConvertView(holder: LabRecyclerViewViewHolder, item: T, position: Int)


    private fun isHasHeaderView(): Boolean = mHeaderView != null

    private fun isHasFooterView(): Boolean = mFooterView != null

    private fun isHasLoadingView(): Boolean = mLoadingView != null

    private fun isHasEmptyView(): Boolean = mEmptyView != null


}

