package com.example.staj.feature.detail.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.staj.databinding.ItemSimilarBinding
import com.example.staj.models.MovieData
import com.example.staj.util.Constants.BASE_IMAGE_URL

class SimilarMovieAdapter(
    private var movies: List<MovieData>
) : RecyclerView.Adapter<SimilarMovieAdapter.ViewHolder>() {
    var onMovieClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSimilarBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = movies.size

    inner class ViewHolder(private val binding: ItemSimilarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val mMovie = movies[position]
            val imageURL = BASE_IMAGE_URL.plus(mMovie.posterPath)

            binding.movieName.text = mMovie.title
            Glide.with(itemView).load(imageURL).into(binding.movieImage)
        }
        init {
            binding.root.setOnClickListener { onMovieClick?.invoke(movies[bindingAdapterPosition].id) }
        }
    }
}
