package com.example.staj.feature.fragments.list.tv

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.staj.R
import com.example.staj.adapters.TvAdapter
import com.example.staj.databinding.FragmentTvListBinding
import com.example.staj.models.TV
import com.example.staj.util.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvListFragment : Fragment() {
    private val viewModel: TvListViewModel by viewModels()
    private lateinit var binding: FragmentTvListBinding
    private lateinit var tvAdapter: TvAdapter
    private var tvList = listOf<TV>()
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTvListBinding.inflate(inflater, container, false)

        showBottomMenu()
        defineRecyclerView()
        defineSearch()

        viewModel.tvLiveData.observe(viewLifecycleOwner) {
            isLoading = false
            setAdapter(it)
        }

        viewModel.searchLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            tvAdapter.setData(it)
        }

        return binding.root
    }

    private fun setAdapter(it: List<TV>) {
        binding.progressBar.visibility = View.GONE

        tvList = it
        Log.d("myLog", "movies size: ".plus(tvList.size))

        tvAdapter.setData(tvList)
    }

    private fun defineRecyclerView() {
        val mLayoutManager = GridLayoutManager(requireContext(), getFromPreferences())
        binding.rvTvShows.layoutManager = mLayoutManager

        tvAdapter = TvAdapter(mLayoutManager)
        binding.rvTvShows.adapter = tvAdapter

        tvAdapter.onTVClick = {
            val action = TvListFragmentDirections.listToDetail(it)
            requireActivity().findNavController(R.id.navHostFragment).navigate(action)
        }

        tvAdapter.onFavouriteClick = { tv, status ->
            viewModel.updateTvFavourite(tv, status)
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
            binding.rvTvShows.scheduleLayoutAnimation()
        }

        binding.rvTvShows.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val layoutManager = (recyclerView.layoutManager as GridLayoutManager)
                val last = layoutManager.findLastVisibleItemPosition()

                if (last >= tvList.size - 1 && !isLoading) {
                    Log.d("myLog", "cant scroll")
                    isLoading = true
                    binding.progressBar.visibility = View.VISIBLE
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
                    viewModel.searchTv(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (newText.isEmpty()) {
                        setAdapter(tvList)
                    }
                }
                return false
            }
        })
    }

    private fun saveToPreferences(spanCount: Int) {
        val preferences =
            requireActivity().getSharedPreferences(Constants.PREFS_FILENAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt(Constants.KEY_SPAN, spanCount)
        editor.apply()
    }

    private fun getFromPreferences(): Int {
        val preferences =
            requireActivity().getSharedPreferences(Constants.PREFS_FILENAME, Context.MODE_PRIVATE)
        return preferences.getInt(Constants.KEY_SPAN, 1)
    }

    private fun showBottomMenu() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.VISIBLE
    }
}
