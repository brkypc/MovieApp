package com.example.staj.adapters

import android.util.Log
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
import com.example.staj.util.Constants
import com.example.staj.util.TvDiffUtil

class TvAdapter(
    private val layoutManager: GridLayoutManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var tvList = emptyList<TV>()
    var onTVClick: ((Int) -> Unit)? = null
    var onFavouriteClick: ((TV, Boolean) -> Unit)? = null

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
        when (holder) {
            is TvAdapter.LinearViewHolder -> holder.bind(position)
            is TvAdapter.GridViewHolder -> holder.bind(position)
        }
    }

    override fun getItemCount() = tvList.size

    override fun getItemViewType(position: Int): Int {
        return when (layoutManager.spanCount) {
            1 -> Constants.LINEAR
            else -> Constants.GRID
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

            if (mTv.isFavourite) {
                Log.d("myLog", "favourite ".plus(mTv.name).plus(position))
                binding.favourite.setImageResource(R.drawable.favourite_filled)
                binding.favourite.tag = "true"
            } else {
                binding.favourite.setImageResource(R.drawable.favourite)
                binding.favourite.tag = "false"
            }
            binding.movieTitle.text = mTv.name
            if (mTv.firstAirDate.isNotEmpty()) {
                binding.movieReleaseDate.text = mTv.firstAirDate.subSequence(
                    Constants.INDEX0,
                    Constants.INDEX4
                )
            } else {
                binding.movieReleaseDate.text = ""
            }
            binding.movieVote.text = MovieData.toCountString(mTv.voteAverage, mTv.voteCount)
            binding.movieLanguage.text = mTv.language.uppercase()
            binding.movieGenre.text = Genre.toGenreString(mTv.genres)

            mTv.posterPath?.let {
                val imageURL = Constants.BASE_IMAGE_URL.plus(it)
                Glide.with(itemView).load(imageURL).into(binding.moviePoster)
            }
        }

        init {
            binding.root.setOnClickListener { onTVClick?.invoke(tvList[adapterPosition].tvID) }
            binding.favourite.setOnClickListener {
                if (binding.favourite.tag.equals("false")) {
                    binding.favourite.setImageResource(R.drawable.favourite_filled)
                    binding.favourite.tag = "true"
                    onFavouriteClick?.invoke(tvList[adapterPosition], true)
                } else {
                    binding.favourite.setImageResource(R.drawable.favourite)
                    binding.favourite.tag = "false"
                    onFavouriteClick?.invoke(tvList[adapterPosition], false)
                }
            }
        }
    }

    inner class GridViewHolder(private val binding: ItemMovieGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val mTv = tvList[position]

            if (mTv.isFavourite) {
                binding.favourite.setImageResource(R.drawable.favourite_filled)
                binding.favourite.tag = "true"
            } else {
                binding.favourite.setImageResource(R.drawable.favourite)
                binding.favourite.tag = "false"
            }

            binding.movieTitle.text = mTv.name
            binding.movieVote.text = mTv.voteAverage.toString()

            mTv.posterPath?.let {
                val imageURL = Constants.BASE_IMAGE_URL.plus(it)
                Glide.with(itemView).load(imageURL).into(binding.moviePoster)
            }
        }

        init {
            binding.root.setOnClickListener { onTVClick?.invoke(tvList[adapterPosition].tvID) }
            binding.favourite.setOnClickListener {
                if (binding.favourite.tag.equals("false")) {
                    binding.favourite.setImageResource(R.drawable.favourite_filled)
                    binding.favourite.tag = "true"
                    onFavouriteClick?.invoke(tvList[adapterPosition], true)
                } else {
                    binding.favourite.setImageResource(R.drawable.favourite)
                    binding.favourite.tag = "false"
                    onFavouriteClick?.invoke(tvList[adapterPosition], false)
                }
            }
        }
    }
}
