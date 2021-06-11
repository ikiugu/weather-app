package com.ikiugu.weather.ui.home

/**
 * Created by Alfred Ikiugu on 09/06/2021
 */

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.*
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.ikiugu.weather.MainActivity
import com.ikiugu.weather.R
import com.ikiugu.weather.databinding.ForecastListItemBinding
import com.ikiugu.weather.databinding.FragmentHomeBinding
import com.ikiugu.weather.domain.ScreenForecast

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var requestingLocationUpdates = false
    private lateinit var locationRequest: LocationRequest
    private val UPDATE_INTERVAL = (10 * 1000 /* 10 secs */).toLong()
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    private lateinit var mMenu: Menu
    private var itemFavorite: Boolean? = null
    private var viewModelAdapter: WeatherForecastAdapter? = null

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

        homeViewModel.appBarColors.observe(viewLifecycleOwner) { color ->
            if (color != null) {
                (activity as MainActivity).handleAppAndStatusBar(color)
            }
        }

        homeViewModel.showSnackBarForBeginApiCall.observe(viewLifecycleOwner) {
            if (it) {
                showSnackBar(R.string.weather_update_loading)
            }
        }

        homeViewModel.showSnackBarForCompletedApiCall.observe(viewLifecycleOwner) {
            if (it) {
                showSnackBar(R.string.weather_update_success)
            }
        }

        homeViewModel.favorite.observe(viewLifecycleOwner) { favorite ->
            if (favorite != null) {
                changeIcon(favorite)

                itemFavorite = favorite

                /* if (favorite) {
                     showSnackBar(R.string.favorite_added)
                 } else {
                     showSnackBar(R.string.favorite_removed)
                 }*/
            }

        }

        enableLocation()

        setHasOptionsMenu(true)

        viewModelAdapter = WeatherForecastAdapter()

        binding.root.findViewById<RecyclerView>(R.id.forecastRecyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.weatherForecast.observe(viewLifecycleOwner) { forecasts ->
            forecasts.apply {
                viewModelAdapter?.screenWeatherForecasts = forecasts
            }
        }
    }

    fun showSnackBar(id: Int) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            getString(id),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    fun enableLocation() {
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            if (activity?.let {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        it,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                } == true
            ) {
                AlertDialog.Builder(context)
                    .setTitle(getString(R.string.permission_location_dialog_title))
                    .setMessage(getString(R.string.permission_location_dialog_description))
                    .setPositiveButton(
                        getString(R.string.ok)
                    ) { _, _ ->
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                requestLocationPermission()
            }
        } else {
            getCurrentLocation()
        }
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.let {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    REQUEST_LOCATION_PERMISSION
                )
            }
        }
        /*else {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
            }
        }*/
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getCurrentLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        requestingLocationUpdates = true
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val loc = Location(location.latitude, location.longitude)
                homeViewModel.location.value = loc
            } else {
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        locationResult ?: return
                        for (location in locationResult.locations) {
                            if (location != null) {
                                fusedLocationClient.removeLocationUpdates(locationCallback)
                                val loc = Location(location.latitude, location.longitude)
                                homeViewModel.location.value = loc
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        if (this::fusedLocationClient.isInitialized) {
            if (this::locationCallback.isInitialized) {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        if (this::locationCallback.isInitialized) {
            locationRequest = LocationRequest()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = UPDATE_INTERVAL
            locationRequest.fastestInterval = FASTEST_INTERVAL
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)

        mMenu = menu

        changeIcon(itemFavorite)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                if (requestingLocationUpdates) {
                    homeViewModel.refreshWeather()
                } else {
                    enableLocation()
                }
            }

            R.id.action_favorite -> homeViewModel.updateWeatherItemToFavorites()
        }

        return super.onOptionsItemSelected(item)
    }

    fun changeIcon(favorite: Boolean?) {
        if (this::mMenu.isInitialized) {
            var menuItem = mMenu.findItem(R.id.action_favorite)
            if (menuItem != null) {
                if (favorite == true) {
                    menuItem.icon = resources.getDrawable(R.drawable.ic_favorite_filled)
                } else if (favorite == false) {
                    menuItem.icon = resources.getDrawable(R.drawable.ic_favorite)
                }
            }
        }
    }

}

class WeatherForecastAdapter() : RecyclerView.Adapter<ForecastDataViewHolder>() {
    var screenWeatherForecasts: List<ScreenForecast> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastDataViewHolder {
        val withDataBinding: ForecastListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ForecastDataViewHolder.LAYOUT,
            parent,
            false
        )
        return ForecastDataViewHolder(withDataBinding)
    }

    override fun getItemCount() = screenWeatherForecasts.size

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: ForecastDataViewHolder, position: Int) {
        holder.viewDataBinding.also { listingBinding ->
            listingBinding.forecastItem = screenWeatherForecasts[position]
        }

        holder.viewDataBinding.forecastIcon.setImageResource(
            when (holder.viewDataBinding.forecastItem?.weatherId) {
                in 200L..299L -> {
                    R.drawable.rain
                }
                in 300L..399L -> {
                    R.drawable.rain
                }
                in 500L..599L -> {
                    R.drawable.rain
                }
                in 600L..699L -> {
                    R.drawable.partlysunny
                }
                in 700L..799L -> {
                    R.drawable.partlysunny
                }
                800L -> {
                    R.drawable.clear
                }
                in 801L..804L -> {
                    R.drawable.partlysunny
                }
                else -> {
                    R.drawable.clear
                }
            }
        )
    }

}

class ForecastDataViewHolder(val viewDataBinding: ForecastListItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.forecast_list_item
    }
}