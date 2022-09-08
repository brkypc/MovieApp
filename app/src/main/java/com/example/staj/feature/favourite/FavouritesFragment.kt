package com.example.staj.feature.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.staj.R
import com.example.staj.databinding.FragmentFavouritesBinding
import com.example.staj.feature.favourite.common.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class FavouritesFragment : Fragment() {
    private lateinit var binding: FragmentFavouritesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        val nameArray = resources.getStringArray(R.array.tab_names)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = nameArray[position]
        }.attach()

        return binding.root
    }
}
