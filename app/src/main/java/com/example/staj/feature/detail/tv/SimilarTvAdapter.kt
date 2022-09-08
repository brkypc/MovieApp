package com.example.staj.feature.detail.tv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.staj.databinding.ItemSimilarBinding
import com.example.staj.models.TVData
import com.example.staj.util.Constants.BASE_IMAGE_URL

class SimilarTvAdapter(
    private var tvList: List<TVData>
) : RecyclerView.Adapter<SimilarTvAdapter.ViewHolder>() {
    var onTVClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSimilarBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = tvList.size

    inner class ViewHolder(private val binding: ItemSimilarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val mTv = tvList[position]
            val imageURL = BASE_IMAGE_URL.plus(mTv.posterPath)

            binding.movieName.text = mTv.name
            Glide.with(itemView).load(imageURL).into(binding.movieImage)
        }

        init {
            binding.root.setOnClickListener { onTVClick?.invoke(tvList[bindingAdapterPosition].id) }
        }
    }
}
