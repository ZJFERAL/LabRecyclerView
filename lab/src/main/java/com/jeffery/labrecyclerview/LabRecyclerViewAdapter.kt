package com.jeffery.labrecyclerview

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 *
 * @Date 2017/11/20  14:18
 * @Author Jeffery
 */
abstract class LabRecyclerViewAdapter<T>(
        protected val mContext: Context,
        protected val mData: ArrayList<T>,
        vararg layoutIds: Int
) : RecyclerView.Adapter<LabRecyclerViewViewHolder>() {

    private val mLayoutIds: IntArray = layoutIds
    private var mRecyclerView: RecyclerView? = null
    private var mRecyclerManager: RecyclerView.LayoutManager? = null
    private var isLoading = false

    companion object {
        private val VIEW_TYPE_HEADER: Int = 0x10000001
        private val VIEW_TYPE_FOOTER: Int = 0x10000002
        private val VIEW_TYPE_LOADING: Int = 0x10000003
    }

    private var mHeaderView: View? = null
    private var mFooterView: View? = null
    private var mLoadingView: View? = null
    private var mEmptyView: View? = null

    fun getData(): List<T> = mData

    fun setHeaderView(view: View) {
        mHeaderView = view
        notifyDataSetChanged()
    }

    fun setFooterView(view: View) {
        mFooterView = view
        notifyDataSetChanged()
    }

    fun setLoadingView(view: View) {
        mLoadingView = view
        isLoading = true
        notifyDataSetChanged()
        mRecyclerView?.smoothScrollToPosition(itemCount - 1)
    }

    fun setEmptyView(view: View) {
        mEmptyView = view
    }

    fun removeHeaderView() {
        if (hasHeaderView()) {
            mHeaderView = null
            notifyDataSetChanged()
        }
    }

    fun removeFooterView() {
        if (hasFooterView()) {
            mFooterView = null
            notifyDataSetChanged()
        }
        isLoading = false
    }

    fun removeLoadingView() {
        if (hasLoadingView()) {
            mLoadingView = null
            notifyDataSetChanged()
        }
    }

    fun removeEmptyView() {
        if (hasEmptyView()) {
            mEmptyView = null
        }
    }

    fun isLoading(): Boolean = isLoading


    private fun hasHeaderView(): Boolean = mHeaderView != null

    private fun hasFooterView(): Boolean = mFooterView != null

    private fun hasLoadingView(): Boolean = mLoadingView != null

    private fun hasEmptyView(): Boolean = mEmptyView != null


    private fun needShowHeaderView(position: Int): Boolean
            = position == 0 && hasHeaderView()

    private fun needShowFooterView(position: Int): Boolean
            = (position == itemCount - 1 && hasFooterView() && !hasLoadingView())
            || (position == itemCount - 2 && hasFooterView() && hasLoadingView())

    private fun needShowLoadingView(position: Int): Boolean
            = position == itemCount - 1 && hasLoadingView()


    override fun getItemCount(): Int {
        setEmptyViewState()
        return mData.size + getLoadingViewCount() + getHeaderViewCount() + getFooterViewCount()
    }

    fun getRealItemCount(): Int {
        return mData.size
    }

    private fun getLoadingViewCount(): Int = if (hasLoadingView()) 1 else 0
    private fun getHeaderViewCount(): Int = if (hasHeaderView()) 1 else 0
    private fun getFooterViewCount(): Int = if (hasFooterView()) 1 else 0

    private fun setEmptyViewState() {
        if (hasEmptyView()) {
            if (getRealItemCount() == 0) {
                mEmptyView?.visibility = View.VISIBLE
                mHeaderView?.visibility = View.GONE
                mFooterView?.visibility = View.GONE
            } else {
                mEmptyView?.visibility = View.GONE
                mHeaderView?.visibility = View.VISIBLE
                mFooterView?.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            needShowHeaderView(position) -> VIEW_TYPE_HEADER
            needShowFooterView(position) -> VIEW_TYPE_FOOTER
            needShowLoadingView(position) -> VIEW_TYPE_LOADING
            else -> getItemType(position)
        }
    }

    protected fun getItemType(position: Int): Int = 0


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        if (mRecyclerManager == null) {
            mRecyclerView = recyclerView
            mRecyclerManager = recyclerView?.layoutManager
            setGridWight(mRecyclerManager)
        }
    }

    private fun setGridWight(manager: RecyclerView.LayoutManager?) {
        if (manager != null) {
            if (manager is GridLayoutManager) {
                manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (needShowHeaderView(position) || needShowFooterView(position) || needShowLoadingView(position)) {
                            manager.spanCount
                        } else 1
                    }
                }
            }
        }
    }


    override fun onBindViewHolder(holder: LabRecyclerViewViewHolder?, position: Int) {
        if (holder != null) {
            if (needShowHeaderView(position) || needShowFooterView(position) || needShowLoadingView(position))return
            setConvertView(holder, mData[position - getHeaderViewCount()],
                    holder.adapterPosition - getHeaderViewCount())
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
            if (mRecyclerManager is StaggeredGridLayoutManager) {
                val targetParams = itemView.layoutParams
                val staggerLayoutParams: StaggeredGridLayoutManager.LayoutParams
                staggerLayoutParams = if (targetParams != null) {
                    StaggeredGridLayoutManager.LayoutParams(targetParams.width, targetParams.height)
                } else {
                    StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                }
                staggerLayoutParams.isFullSpan = true
                itemView.layoutParams = staggerLayoutParams
            }
            holder = LabRecyclerViewViewHolder.createViewHolder(mContext, itemView, viewType)
        }

        return holder
    }

    abstract fun setConvertView(holder: LabRecyclerViewViewHolder, item: T, position: Int)


    /**
     * 添加单条数据
     *
     * @param position
     * @param t
     */
    fun addItemData(position: Int, t: T) {
        mData.add(position, t)
        notifyItemInserted(position)
    }

    /**
     * 添加单条数据
     *
     * @param t
     */
    fun addItemData(t: T) {
        val size = mData.size
        mData.add(size, t)
        notifyItemInserted(size)
    }

    /**
     * 移除单条数据
     *
     * @param position
     */
    fun removeItemData(position: Int) {
        mData.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * 修改单条数据
     *
     * @param position
     * @param t
     */
    fun changItemData(position: Int, t: T) {
        mData.set(position, t)
        notifyItemChanged(position)
    }

    /**
     * 刷新数据
     *
     * @param newData
     */
    fun flushData(newData: List<T>) {
        mData.clear()
        this.addNewData(newData)
    }

    /**
     * 添加新数据集合
     *
     * @param newData
     */
    fun addNewData(newData: List<T>) {
        mData.addAll(newData)
        notifyDataSetChanged()
    }

}

