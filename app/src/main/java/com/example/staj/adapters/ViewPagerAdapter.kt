package com.example.staj.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.staj.feature.fragments.favourite.movie.FavouriteMoviesFragment
import com.example.staj.feature.fragments.favourite.tv.FavouriteTvFragment
import com.example.staj.util.Constants.TAB_SIZE

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = TAB_SIZE

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavouriteMoviesFragment()
            else -> FavouriteTvFragment()
        }
    }
}
