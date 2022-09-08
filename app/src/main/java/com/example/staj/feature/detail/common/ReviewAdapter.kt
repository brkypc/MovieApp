package com.example.staj.feature.detail.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.staj.R
import com.example.staj.databinding.ItemReviewBinding
import com.example.staj.models.Review

class ReviewAdapter(
    private var reviews: List<Review>
) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = reviews.size

    inner class ViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val mReview = reviews[position]

            binding.reviewName.text =
                itemView.context.getString(R.string.at_sign).plus(mReview.author)
            binding.reviewContent.text = mReview.content
            mReview.authorDetails.avatarPath?.let {
                Glide.with(itemView).load(it.removeRange(0, 1)).into(binding.reviewImage)
            }
        }
    }
}
