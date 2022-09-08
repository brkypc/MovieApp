package com.example.staj.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.staj.R
import com.example.staj.databinding.ItemMovieBinding
import com.example.staj.databinding.ItemMovieGridBinding
import com.example.staj.models.Genre
import com.example.staj.models.Movie
import com.example.staj.models.MovieData
import com.example.staj.util.Constants

class MovieAdapterPaging(
    private val layoutManager: GridLayoutManager
) : PagingDataAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback) {

    var onMovieClick: ((Int) -> Unit)? = null
    var onFavouriteClick: ((Movie, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.LINEAR -> {
                LinearViewHolder(
                    ItemMovieBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                GridViewHolder(
                    ItemMovieGridBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { movie ->
            when (holder) {
                is LinearViewHolder -> holder.bind(movie)
                is GridViewHolder -> holder.bind(movie)
            }
        }
    }

    object MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.movieID == newItem.movieID
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (layoutManager.spanCount) {
            1 -> Constants.LINEAR
            else -> Constants.GRID
        }
    }

    inner class LinearViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mMovie: Movie) {

            if (mMovie.isFavourite) {
                binding.favourite.setImageResource(R.drawable.favourite_filled)
                binding.favourite.tag = "true"
            } else {
                binding.favourite.setImageResource(R.drawable.favourite)
                binding.favourite.tag = "false"
            }
            binding.movieTitle.text = mMovie.title
            if (mMovie.releaseDate.isNotEmpty()) {
                binding.movieReleaseDate.text = mMovie.releaseDate.subSequence(
                    Constants.INDEX0,
                    Constants.INDEX4
                )
            } else {
                binding.movieReleaseDate.text = ""
            }
            binding.movieVote.text = MovieData.toCountString(mMovie.voteAverage, mMovie.voteCount)
            binding.movieLanguage.text = mMovie.language.uppercase()
            binding.movieGenre.text = Genre.toGenreString(mMovie.genres)

            mMovie.posterPath?.let {
                val imageURL = Constants.BASE_IMAGE_URL.plus(it)
                Glide.with(itemView).load(imageURL).into(binding.moviePoster)
            }
        }

        init {
            binding.root.setOnClickListener {
                getItem(bindingAdapterPosition)?.let {
                    onMovieClick?.invoke(it.movieID)
                }
            }
            binding.favourite.setOnClickListener {
                if (binding.favourite.tag.equals("false")) {
                    binding.favourite.setImageResource(R.drawable.favourite_filled)
                    binding.favourite.tag = "true"
                    getItem(bindingAdapterPosition)?.let {
                        onFavouriteClick?.invoke(it, true)
                    }
                } else {
                    binding.favourite.setImageResource(R.drawable.favourite)
                    binding.favourite.tag = "false"
                    getItem(bindingAdapterPosition)?.let {
                        onFavouriteClick?.invoke(it, false)
                    }
                }
            }
        }

    }

    inner class GridViewHolder(private val binding: ItemMovieGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mMovie: Movie) {

            if (mMovie.isFavourite) {
                binding.favourite.setImageResource(R.drawable.favourite_filled)
                binding.favourite.tag = "true"
            } else {
                binding.favourite.setImageResource(R.drawable.favourite)
                binding.favourite.tag = "false"
            }

            binding.movieTitle.text = mMovie.title
            binding.movieVote.text = mMovie.voteAverage.toString()

            mMovie.posterPath?.let {
                val imageURL = Constants.BASE_IMAGE_URL.plus(it)
                Glide.with(itemView).load(imageURL).into(binding.moviePoster)
            }
        }

        init {
            binding.root.setOnClickListener {
                getItem(bindingAdapterPosition)?.let {
                    onMovieClick?.invoke(it.movieID)
                }
            }
            binding.favourite.setOnClickListener {
                if (binding.favourite.tag.equals("false")) {
                    binding.favourite.setImageResource(R.drawable.favourite_filled)
                    binding.favourite.tag = "true"
                    getItem(bindingAdapterPosition)?.let {
                        onFavouriteClick?.invoke(it, true)
                    }
                } else {
                    binding.favourite.setImageResource(R.drawable.favourite)
                    binding.favourite.tag = "false"
                    getItem(bindingAdapterPosition)?.let {
                        onFavouriteClick?.invoke(it, false)
                    }
                }
            }
        }

    }
}