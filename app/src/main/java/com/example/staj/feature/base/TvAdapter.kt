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
import com.example.staj.models.MovieData
import com.example.staj.models.TV
import com.example.staj.util.Constants.BASE_IMAGE_URL
import com.example.staj.util.Constants.GRID
import com.example.staj.util.Constants.INDEX0
import com.example.staj.util.Constants.INDEX4
import com.example.staj.util.Constants.LINEAR
import com.example.staj.util.TvDiffUtil

class TvAdapter(
    private val layoutManager: GridLayoutManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onTVClick: ((Int, Int) -> Unit)? = null
    var onFavouriteClick: ((TV, Boolean) -> Unit)? = null
    private var tvList = emptyList<TV>()

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

    override fun getItemCount() = tvList.size

    override fun getItemViewType(position: Int): Int {
        return when (layoutManager.spanCount) {
            1 -> LINEAR
            else -> GRID
        }
    }

    fun setData(newList: List<TV>) {
        val diffUtilCallback = TvDiffUtil(tvList, newList)
        val diffResults = DiffUtil.calculateDiff(diffUtilCallback)
        tvList = newList
        diffResults.dispatchUpdatesTo(this)
    }

    inner class LinearViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val mTv = tvList[position]

            binding.favourite.apply {
                tag = if (mTv.isFavourite) {
                    setImageResource(R.drawable.favourite_filled)
                    "true"
                } else {
                    setImageResource(R.drawable.favourite)
                    "false"
                }
            }

            binding.movieTitle.text = mTv.name
            if (mTv.firstAirDate.isNotEmpty()) {
                binding.movieReleaseDate.text = mTv.firstAirDate.subSequence(INDEX0, INDEX4)
            } else {
                binding.movieReleaseDate.text = ""
            }

            binding.movieVote.text = MovieData.toCountString(mTv.voteAverage, mTv.voteCount)
            binding.movieLanguage.text = mTv.language.uppercase()
            binding.movieGenre.text = Genre.toGenreString(mTv.genres)

            mTv.posterPath?.let {
                val imageURL = BASE_IMAGE_URL.plus(it)
                Glide.with(itemView).load(imageURL).into(binding.moviePoster)
            }
        }

        init {
            binding.root.setOnClickListener {
                onTVClick?.invoke(tvList[bindingAdapterPosition].tvID, bindingAdapterPosition)
            }
            binding.favourite.apply {
                setOnClickListener {
                    if (tag.equals("false")) {
                        setImageResource(R.drawable.favourite_filled)
                        tag = "true"
                        onFavouriteClick?.invoke(tvList[bindingAdapterPosition], true)
                    } else {
                        setImageResource(R.drawable.favourite)
                        tag = "false"
                        onFavouriteClick?.invoke(tvList[bindingAdapterPosition], false)
                    }
                }
            }
        }
    }

    inner class GridViewHolder(private val binding: ItemMovieGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val mTv = tvList[position]

            binding.favourite.apply {
                tag = if (mTv.isFavourite) {
                    setImageResource(R.drawable.favourite_filled)
                    "true"
                } else {
                    setImageResource(R.drawable.favourite)
                    "false"
                }
            }

            binding.movieTitle.text = mTv.name
            binding.movieVote.text = mTv.voteAverage.toString()

            mTv.posterPath?.let {
                val imageURL = BASE_IMAGE_URL.plus(it)
                Glide.with(itemView).load(imageURL).into(binding.moviePoster)
            }
        }

        init {
            binding.root.setOnClickListener {
                onTVClick?.invoke(tvList[bindingAdapterPosition].tvID, bindingAdapterPosition)
            }
            binding.favourite.apply {
                setOnClickListener {
                    if (tag.equals("false")) {
                        setImageResource(R.drawable.favourite_filled)
                        tag = "true"
                        onFavouriteClick?.invoke(tvList[bindingAdapterPosition], true)
                    } else {
                        setImageResource(R.drawable.favourite)
                        tag = "false"
                        onFavouriteClick?.invoke(tvList[bindingAdapterPosition], false)
                    }
                }
            }
        }
    }
}
