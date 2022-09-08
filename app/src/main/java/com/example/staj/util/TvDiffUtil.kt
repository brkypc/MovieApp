package com.example.staj.util

import androidx.recyclerview.widget.DiffUtil
import com.example.staj.models.TV

class TvDiffUtil(
    private val oldList: List<TV>,
    private val newList: List<TV>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].tvID == newList[newItemPosition].tvID
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].isFavourite == newList[newItemPosition].isFavourite
    }
}
