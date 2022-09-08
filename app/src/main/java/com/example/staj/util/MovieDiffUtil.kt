package com.example.staj.util

import androidx.recyclerview.widget.DiffUtil
import com.example.staj.models.Movie

class MovieDiffUtil(
    private val oldList: List<Movie>,
    private val newList: List<Movie>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].movieID == newList[newItemPosition].movieID
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].isFavourite == newList[newItemPosition].isFavourite
    }
}
