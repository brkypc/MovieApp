package com.example.staj.feature.detail.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.staj.databinding.ItemCastBinding
import com.example.staj.models.Cast
import com.example.staj.util.Constants.BASE_IMAGE_URL

class CastAdapter(
    private var cast: List<Cast>
) : RecyclerView.Adapter<CastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = cast.size

    inner class ViewHolder(private val binding: ItemCastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val mCast = cast[position]
            val imageURL = BASE_IMAGE_URL.plus(mCast.profilePath)

            binding.castName.text = mCast.name
            Glide.with(itemView).load(imageURL).into(binding.castImage)
        }
    }
}
