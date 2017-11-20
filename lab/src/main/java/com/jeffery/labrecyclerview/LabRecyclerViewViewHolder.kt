package com.jeffery.labrecyclerview

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

/**
 *
 * @Date 2017/11/20  14:45
 * @Author Jeffery
 */
class LabRecyclerViewViewHolder private constructor(
        private val mContext: Context,
        private val mItemView: View,
        private val mType: Int
) : RecyclerView.ViewHolder(mItemView) {
    private var mViews: SparseArray<View> = SparseArray()

    companion object {
        fun createViewHolder(context: Context, itemView: View, type: Int)
                : LabRecyclerViewViewHolder
                = LabRecyclerViewViewHolder(context, itemView, type)


        fun createViewHolder(context: Context, parent: ViewGroup, layoutId: Int, type: Int)
                : LabRecyclerViewViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutId, parent, false)
            return createViewHolder(context, itemView, type)
        }
    }

    fun getType(): Int = mType

    fun getItemView(): View = mItemView

    fun <T : View> getView(viewId: Int): T {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = mItemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T
    }

    fun setText(viewId: Int, textStr: String): LabRecyclerViewViewHolder {
        getView<TextView>(viewId).text = textStr
        return this
    }

    fun setClickListener(viewId: Int, listener: View.OnClickListener): LabRecyclerViewViewHolder {
        getView<View>(viewId).setOnClickListener(listener)
        return this
    }

    fun setImageResource(viewId: Int, resourceId: Int): LabRecyclerViewViewHolder {
        getView<ImageView>(viewId).setImageResource(resourceId)
        return this
    }

    fun setImageBitmap(viewId: Int, bitmap: Bitmap): LabRecyclerViewViewHolder {
        getView<ImageView>(viewId).setImageBitmap(bitmap)
        return this
    }

    fun setImageByUrl(viewId: Int, url: String, with: Int = -1, height: Int = -1, placeHolderResId: Int = -1): LabRecyclerViewViewHolder {
        var picasso = Picasso.with(mContext).load(url)
        if (with != -1 && height != -1) {
            picasso = picasso.resize(with, height)
        }
        if (placeHolderResId != -1) {
            picasso = picasso.placeholder(placeHolderResId)
        }
        picasso.into(getView<ImageView>(viewId))
        return this
    }

}