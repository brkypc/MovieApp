package com.example.staj.feature.fragments.favourite.tv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.staj.R
import com.example.staj.adapters.TvAdapter
import com.example.staj.databinding.FragmentFavouriteMoviesBinding
import com.example.staj.feature.fragments.favourite.FavouritesFragmentDirections
import com.example.staj.models.TV
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteTvFragment : Fragment() {
    private val viewModel: FavouriteTvViewModel by viewModels()
    private lateinit var binding: FragmentFavouriteMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteMoviesBinding.inflate(inflater, container, false)

        showBottomMenu()

        viewModel.getTv().observe(viewLifecycleOwner) {
            defineRecyclerView(it)
        }

        return binding.root
    }

    private fun defineRecyclerView(tvList: List<TV>) {
        if (tvList.isEmpty()) {
            binding.noFavourites.visibility = View.VISIBLE
            binding.noFavourites.text = getString(R.string.add_tv)
        }

        binding.rvMovies.scheduleLayoutAnimation()
        val mLayoutManager = GridLayoutManager(requireContext(), 1)
        binding.rvMovies.layoutManager = mLayoutManager

        val tvAdapter = TvAdapter(mLayoutManager)
        tvAdapter.setData(tvList)
        binding.rvMovies.adapter = tvAdapter

        tvAdapter.onTVClick = {
            val action = FavouritesFragmentDirections.favouriteToDetailTV(it)
            requireActivity().findNavController(R.id.navHostFragment).navigate(action)
            hideBottomMenu()
        }

        tvAdapter.onFavouriteClick = { tv, _ ->
            viewModel.updateTvFavourite(tv)
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
