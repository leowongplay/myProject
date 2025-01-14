package com.example.comp4342mobilecomputinggroupproject.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.comp4342mobilecomputinggroupproject.R
import com.example.comp4342mobilecomputinggroupproject.adapters.HomeViewpagerAdapter
import com.example.comp4342mobilecomputinggroupproject.databinding.FragmentHomeBinding
import com.example.comp4342mobilecomputinggroupproject.fragments.categories.*
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment: Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val  categoriesFragments = arrayListOf<Fragment>(
            MainCategory(),
            Shoes(),
            Clothes(),
            Bags(),
            Hats(),
            SportEquipments()
        )

        binding.viewPagerHome.isUserInputEnabled = false

        val viewPager2Adapter = HomeViewpagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
        binding.viewPagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPagerHome){ tab, position ->
            when(position){
                0 -> tab.text = "Main"
                1 -> tab.text = "Shoes"
                2 -> tab.text = "Clothes"
                3 -> tab.text = "Bags"
                4 -> tab.text = "Hats"
                5 -> tab.text = "Sport Equipments"
            }
        }.attach()
        binding.searchbutton.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }
}