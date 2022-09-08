package com.example.staj.feature.fragments.detail.tv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.staj.R
import com.example.staj.adapters.CastAdapter
import com.example.staj.adapters.ReviewAdapter
import com.example.staj.databinding.FragmentMovieDetailBinding
import com.example.staj.models.Cast
import com.example.staj.models.MovieData
import com.example.staj.models.Review
import com.example.staj.models.TV
import com.example.staj.util.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvDetailFragment : Fragment() {
    private val viewModel: TvDetailViewModel by viewModels()
    private val args: TvDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentMovieDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        hideBottomMenu()

        viewModel.tvLiveData.observe(viewLifecycleOwner) {
            binding.lottie.visibility = View.GONE
            binding.appBar.visibility = View.VISIBLE
            binding.nestedScrollView.visibility = View.VISIBLE

            it?.let { defineFields(it) }
        }
        viewModel.getTv(args.tvID)

        return binding.root
    }

    private fun defineFields(tv: TV) {
        tv.posterPath?.let {
            Glide.with(requireContext()).load(Constants.BASE_IMAGE_URL.plus(it))
                .into(binding.moviePoster)
        }

        binding.run {
            binding.favourite.tag = "false"
            if (tv.isFavourite) {
                binding.favourite.setImageResource(R.drawable.favourite_filled)
                binding.favourite.tag = "true"
            }

            movieName.text = tv.name
            movieRuntime.text = tv.episodeRunTime.toString()
                .plus(" minutes") // nullable // check if count is necessary
            val voteText = MovieData.toCountString(tv.voteAverage, tv.voteCount)
            movieRating.text = voteText
            movieReleaseDate.text = tv.firstAirDate
            movieOverview.text = tv.overview

            when (tv.genres.size) {
                1 -> movieGenre1.text = tv.genres[0].name
                2 -> {
                    movieGenre1.text = tv.genres[0].name

                    genreCard2.visibility = View.VISIBLE
                    movieGenre2.text = tv.genres[1].name
                }
                else -> {
                    movieGenre1.text = tv.genres[0].name

                    genreCard2.visibility = View.VISIBLE
                    movieGenre2.text = tv.genres[1].name

                    genreCard3.visibility = View.VISIBLE
                    movieGenre3.text = tv.genres[2].name
                }
            }
        }

        binding.favourite.setOnClickListener {
            if (binding.favourite.tag.equals("false")) {
                binding.favourite.setImageResource(R.drawable.favourite_filled)
                binding.favourite.tag = "true"
                viewModel.updateTvFavourite(tv, true)
            } else {
                binding.favourite.setImageResource(R.drawable.favourite)
                binding.favourite.tag = "false"
                viewModel.updateTvFavourite(tv, false)
            }
        }

        tv.cast?.let { defineCast(it) }

        tv.reviews?.let { defineReviews(it) }
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

    private fun hideBottomMenu() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.INVISIBLE
    }
}
