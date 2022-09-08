package com.example.staj.feature.fragments.favourite.movie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.staj.R
import com.example.staj.adapters.MovieAdapter
import com.example.staj.databinding.FragmentFavouriteMoviesBinding
import com.example.staj.feature.fragments.favourite.FavouritesFragmentDirections
import com.example.staj.models.Movie
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteMoviesFragment : Fragment() {
    private val viewModel: FavouriteMoviesViewModel by viewModels()
    private lateinit var binding: FragmentFavouriteMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteMoviesBinding.inflate(inflater, container, false)

        showBottomMenu()

        viewModel.getMovies().observe(viewLifecycleOwner) {
            defineRecyclerView(it)
        }

        return binding.root
    }

    private fun defineRecyclerView(movies: List<Movie>) {
        if (movies.isEmpty()) binding.noFavourites.visibility = View.VISIBLE

        binding.rvMovies.scheduleLayoutAnimation()
        val mLayoutManager = GridLayoutManager(requireContext(), 1)
        binding.rvMovies.layoutManager = mLayoutManager

        val movieAdapter = MovieAdapter(mLayoutManager)
        movieAdapter.setData(movies)
        binding.rvMovies.adapter = movieAdapter

        movieAdapter.onMovieClick = {
            val action = FavouritesFragmentDirections.favouriteToDetailMovie(it)
            requireActivity().findNavController(R.id.navHostFragment).navigate(action)
            hideBottomMenu()
        }

        movieAdapter.onFavouriteClick = { movie, _ ->
            viewModel.updateMovieFavourite(movie)
        }
    }

    private fun showBottomMenu() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.VISIBLE
    }

    private fun hideBottomMenu() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.INVISIBLE
    }
}
