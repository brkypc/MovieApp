package com.example.staj.feature.base

import android.view.LayoutInflater
import android.view.ViewGroup
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
import com.example.staj.util.Constants.BASE_IMAGE_URL
import com.example.staj.util.Constants.GRID
import com.example.staj.util.Constants.INDEX0
import com.example.staj.util.Constants.INDEX4
import com.example.staj.util.Constants.LINEAR
import com.example.staj.util.MovieDiffUtil

class MovieAdapter(
    private val layoutManager: GridLayoutManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onMovieClick: ((Int, Int) -> Unit)? = null
    var onFavouriteClick: ((Movie, Boolean) -> Unit)? = null
    private var movies = emptyList<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LINEAR -> {
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
        when (holder) {
            is LinearViewHolder -> holder.bind(position)
            is GridViewHolder -> holder.bind(position)
        }
    }

    override fun getItemCount() = movies.size

    override fun getItemViewType(position: Int): Int {
        return when (layoutManager.spanCount) {
            1 -> LINEAR
            else -> GRID
        }
    }

    fun setData(newList: List<Movie>) {
        val diffUtilCallback = MovieDiffUtil(movies, newList)
        val diffResults = DiffUtil.calculateDiff(diffUtilCallback)
        movies = newList
        diffResults.dispatchUpdatesTo(this)
    }

    inner class LinearViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val mMovie = movies[position]

            binding.favourite.apply {
                tag = if (mMovie.isFavourite) {
                    setImageResource(R.drawable.favourite_filled)
                    "true"
                } else {
                    setImageResource(R.drawable.favourite)
                    "false"
                }
            }

            binding.movieTitle.text = mMovie.title
            if (mMovie.releaseDate.isNotEmpty()) {
                binding.movieReleaseDate.text = mMovie.releaseDate.subSequence(INDEX0, INDEX4)
            } else {
                binding.movieReleaseDate.text = ""
            }

            binding.movieVote.text = MovieData.toCountString(mMovie.voteAverage, mMovie.voteCount)
            binding.movieLanguage.text = mMovie.language.uppercase()
            binding.movieGenre.text = Genre.toGenreString(mMovie.genres)

            mMovie.posterPath?.let {
                val imageURL = BASE_IMAGE_URL.plus(it)
                Glide.with(itemView).load(imageURL).into(binding.moviePoster)
            }
        }

        init {
            binding.root.setOnClickListener {
                onMovieClick?.invoke(movies[bindingAdapterPosition].movieID, bindingAdapterPosition)
            }
            binding.favourite.apply {
                setOnClickListener {
                    if (tag.equals("false")) {
                        setImageResource(R.drawable.favourite_filled)
                        tag = "true"
                        onFavouriteClick?.invoke(movies[bindingAdapterPosition], true)
                    } else {
                        setImageResource(R.drawable.favourite)
                        tag = "false"
                        onFavouriteClick?.invoke(movies[bindingAdapterPosition], false)
                    }
                }
            }
        }
    }

    inner class GridViewHolder(private val binding: ItemMovieGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val mMovie = movies[position]

            binding.favourite.apply {
                tag = if (mMovie.isFavourite) {
                    setImageResource(R.drawable.favourite_filled)
                    "true"
                } else {
                    setImageResource(R.drawable.favourite)
                    "false"
                }
            }

            binding.movieTitle.text = mMovie.title
            binding.movieVote.text = mMovie.voteAverage.toString()

            mMovie.posterPath?.let {
                val imageURL = BASE_IMAGE_URL.plus(it)
                Glide.with(itemView).load(imageURL).into(binding.moviePoster)
            }
        }

        init {
            binding.root.setOnClickListener {
                onMovieClick?.invoke(movies[bindingAdapterPosition].movieID, bindingAdapterPosition)
            }
            binding.favourite.apply {
                setOnClickListener {
                    if (tag.equals("false")) {
                        setImageResource(R.drawable.favourite_filled)
                        tag = "true"
                        onFavouriteClick?.invoke(movies[bindingAdapterPosition], true)
                    } else {
                        setImageResource(R.drawable.favourite)
                        tag = "false"
                        onFavouriteClick?.invoke(movies[bindingAdapterPosition], false)
                    }
                }
            }
        }
    }
}
