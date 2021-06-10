package com.ikiugu.weather.ui.home

/**
 * Created by Alfred Ikiugu on 09/06/2021
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ikiugu.weather.MainActivity
import com.ikiugu.weather.R
import com.ikiugu.weather.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = homeViewModel

        homeViewModel.appColors.observe(viewLifecycleOwner) { color ->
            if (color != null) {
                (activity as MainActivity).handleAppAndStatusBar(color)
            }
        }



        return binding.root
    }

}