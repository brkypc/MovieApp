package com.example.staj.feature.list.movie

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.staj.R
import com.example.staj.databinding.FragmentMovieListBinding
import com.example.staj.feature.base.MovieAdapter
import com.example.staj.models.Movie
import com.example.staj.util.Constants.KEY_SPAN
import com.example.staj.util.Constants.PREFS_FILENAME
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment() {
    private val viewModel: MovieListViewModel by viewModels()
    private lateinit var binding: FragmentMovieListBinding
    private lateinit var movieAdapter: MovieAdapter
    private var movies = listOf<Movie>()
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)

        showBottomMenu()
        initializeView()
        defineSearch()

        viewModel.searchLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            movieAdapter.setData(it)
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            isLoading = false
            binding.progressBar.visibility = View.GONE
            makeToast()
        }

        viewModel.movieLiveData.observe(viewLifecycleOwner) {
            isLoading = false
            setAdapter(it)
        }
        viewModel.checkUpdate()

        return binding.root
    }

    private fun setAdapter(it: List<Movie>) {
        binding.progressBar.visibility = View.GONE
        movies = it
        movieAdapter.setData(movies)
    }

    private fun initializeView() {
        binding.lottie.apply {
            setOnClickListener { playAnimation() }
        }

        val mLayoutManager = GridLayoutManager(requireContext(), getFromPreferences())
        binding.rvMovies.layoutManager = mLayoutManager

        movieAdapter = MovieAdapter(mLayoutManager)
        binding.rvMovies.adapter = movieAdapter

        movieAdapter.onMovieClick = { movieId, pos ->
            viewModel.moviePos = pos
            val action = MovieListFragmentDirections.listToDetail(movieId)
            requireActivity().findNavController(R.id.navHostFragment).navigate(action)
        }

        movieAdapter.onFavouriteClick = { movie, status ->
            viewModel.updateMovieFavourite(movie, status)
        }

        binding.changeLayout.setOnClickListener {
            if (mLayoutManager.spanCount == 1) {
                mLayoutManager.spanCount = 2
                binding.changeLayout.setImageResource(R.drawable.grid_layout)
            } else {
                mLayoutManager.spanCount = 1
                binding.changeLayout.setImageResource(R.drawable.linear_layout)
            }

            saveToPreferences(mLayoutManager.spanCount)
            binding.rvMovies.scheduleLayoutAnimation()
        }

        binding.rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val layoutManager = (recyclerView.layoutManager as GridLayoutManager)
                val last = layoutManager.findLastVisibleItemPosition()

                if (last >= movies.size - 1 && !isLoading) {
                    isLoading = true
                    binding.progressBar.visibility = View.VISIBLE
                    binding.lottie.playAnimation()

                    viewModel.onNextPage()
                }
            }
        })
    }

    private fun defineSearch() {
        binding.searchMovie.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    binding.progressBar.visibility = View.VISIBLE
                    viewModel.searchMovies(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                setAdapter(movies)
                return false
            }
        })
    }

    private fun saveToPreferences(spanCount: Int) {
        val preferences =
            requireActivity().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt(KEY_SPAN, spanCount)
        editor.apply()
    }

    private fun getFromPreferences(): Int {
        val preferences =
            requireActivity().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        return preferences.getInt(KEY_SPAN, 1)
    }

    private fun makeToast() {
        Toast.makeText(requireContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show()
    }

    private fun showBottomMenu() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.VISIBLE
    }
}
