package com.example.staj.feature.detail.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.staj.R
import com.example.staj.databinding.FragmentMovieDetailBinding
import com.example.staj.feature.detail.common.CastAdapter
import com.example.staj.feature.detail.common.ReviewAdapter
import com.example.staj.feature.detail.common.TrailerAdapter
import com.example.staj.models.Cast
import com.example.staj.models.Movie
import com.example.staj.models.MovieData
import com.example.staj.models.Review
import com.example.staj.models.Video
import com.example.staj.util.Constants.BASE_IMAGE_URL
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {
    private val viewModel: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentMovieDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        hideBottomMenu()

        viewModel.movieLiveData.observe(viewLifecycleOwner) {
            binding.lottie.visibility = View.GONE
            binding.appBar.visibility = View.VISIBLE
            binding.nestedScrollView.visibility = View.VISIBLE

            it?.let { defineFields(it) }
        }
        viewModel.getMovie(args.movieID)

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            makeToast()
        }

        return binding.root
    }

    private fun defineFields(movie: Movie) {
        movie.posterPath?.let {
            Glide.with(requireContext()).load(BASE_IMAGE_URL.plus(it))
                .into(binding.moviePoster)
        }

        binding.run {
            binding.favourite.tag = "false"
            if (movie.isFavourite) {
                binding.favourite.setImageResource(R.drawable.favourite_filled)
                binding.favourite.tag = "true"
            }

            movieName.text = movie.title
            movieRuntime.text = movie.runtime.toString().plus(" minutes")
            val voteText = MovieData.toCountString(movie.voteAverage, movie.voteCount)
            movieRating.text = voteText
            movieReleaseDate.text = movie.releaseDate
            movieOverview.text = movie.overview

            when (movie.genres.size) {
                1 -> movieGenre1.text = movie.genres[0].name
                2 -> {
                    movieGenre1.text = movie.genres[0].name

                    genreCard2.visibility = View.VISIBLE
                    movieGenre2.text = movie.genres[1].name
                }
                else -> {
                    movieGenre1.text = movie.genres[0].name

                    genreCard2.visibility = View.VISIBLE
                    movieGenre2.text = movie.genres[1].name

                    genreCard3.visibility = View.VISIBLE
                    movieGenre3.text = movie.genres[2].name
                }
            }
        }

        binding.favourite.setOnClickListener {
            if (binding.favourite.tag.equals("false")) {
                binding.favourite.setImageResource(R.drawable.favourite_filled)
                binding.favourite.tag = "true"
                viewModel.updateMovieFavourite(movie, true)
            } else {
                binding.favourite.setImageResource(R.drawable.favourite)
                binding.favourite.tag = "false"
                viewModel.updateMovieFavourite(movie, false)
            }
        }

        movie.cast?.let { defineCast(it) }
        movie.reviews?.let { defineReviews(it) }
        movie.similar?.let { defineSimilar(it) }
        movie.videos?.let { defineTrailer(it) }
    }

    private fun defineTrailer(videos: List<Video>) {
        var isInitialized = false

        binding.showTrailers.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (!isInitialized) {
                    isInitialized = true

                    val snapHelper = LinearSnapHelper()
                    snapHelper.attachToRecyclerView(binding.rvTrailer)

                    binding.rvTrailer.layoutManager =
                        LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

                    val trailerAdapter = TrailerAdapter(videos)
                    binding.rvTrailer.adapter = trailerAdapter
                }
                binding.rvTrailer.visibility = View.VISIBLE
            } else {
                binding.rvTrailer.visibility = View.GONE
            }
        }
    }

    private fun defineSimilar(similar: List<MovieData>) {
        binding.rvSimilar.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        val similarAdapter = SimilarMovieAdapter(similar)
        similarAdapter.onMovieClick = {
            val action = MovieDetailFragmentDirections.detailToDetail(it)
            requireActivity().findNavController(R.id.navHostFragment).navigate(action)
        }
        binding.rvSimilar.adapter = similarAdapter
    }

    private fun defineReviews(reviewList: List<Review>) {
        binding.reviewsText.visibility = View.VISIBLE
        binding.rvReviews.visibility = View.VISIBLE

        binding.rvReviews.layoutManager = LinearLayoutManager(requireContext())

        val reviewAdapter = ReviewAdapter(reviewList)
        binding.rvReviews.adapter = reviewAdapter
    }

    private fun defineCast(cast: List<Cast>) {
        binding.rvCast.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        val castAdapter = CastAdapter(cast)
        binding.rvCast.adapter = castAdapter
    }

    private fun makeToast() {
        Toast.makeText(requireContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show()
    }

    private fun hideBottomMenu() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.INVISIBLE
    }
}
