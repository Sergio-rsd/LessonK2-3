package ru.gb.kotlinapp.view

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.gb.kotlinapp.R
import ru.gb.kotlinapp.databinding.MainFragmentBinding
import ru.gb.kotlinapp.model.City
import ru.gb.kotlinapp.model.Weather
import ru.gb.kotlinapp.model.getRussianCities
import ru.gb.kotlinapp.model.getWorldCities
import ru.gb.kotlinapp.util.*
import ru.gb.kotlinapp.view.details.DetailsFragment
import ru.gb.kotlinapp.viewmodel.AppState
import ru.gb.kotlinapp.viewmodel.MainViewModel
import java.io.File
import java.io.IOException

private const val IS_WORLD_KEY = "IS_WORLD_KEY"
private const val LIST_OF_TOWNS = "LIST_OF_TOWNS"
private const val CITY_EXIST = "CITY_EXIST"
private const val IS_CITY_KEY = "IS_CITY_KEY"
private const val REQUEST_CODE = 12345
private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private var isDataSetRus: Boolean = true
    private val myTreadHandler = MyThread()

    companion object {
        fun newInstance() = MainFragment()
    }

    private val adapter = MainFragmentAdapter(object : MainFragmentAdapter.OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            toDetailsWeather(weather)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }

        binding.mainFragmentFABLocation.setOnClickListener { checkPermission() }

        val observer = Observer<AppState> {
            renderData(it)
        }

        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        showListOfTowns()
    }

    private fun saveListOfTowns() {
        activity?.let {
            with(it.getSharedPreferences(LIST_OF_TOWNS, Context.MODE_PRIVATE).edit()) {
                putBoolean(IS_WORLD_KEY, !isDataSetRus)
                apply()
            }
        }
    }

    private fun showListOfTowns() {
        activity?.let {
            if (it.getSharedPreferences(LIST_OF_TOWNS, Context.MODE_PRIVATE)
                    .getBoolean(IS_WORLD_KEY, false)
            ) {
                changeWeatherDataSet()
            } else {
                if (showFavoriteCities() == true) {
                    viewModel.getWeatherFromLocalSourceRusFavorite()
                } else {
                    viewModel.getWeatherFromLocalSourceRus()
                }
            }
        }
    }

    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            if (showFavoriteCities() == true) {
                viewModel.getWeatherFromLocalSourceWorldFavorite()
            } else {
                viewModel.getWeatherFromLocalSourceWorld()
            }
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        } else {
            if (showFavoriteCities() == true) {
                viewModel.getWeatherFromLocalSourceRusFavorite()
            } else {
                viewModel.getWeatherFromLocalSourceRus()
            }
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        }.also { isDataSetRus = !isDataSetRus }
        saveListOfTowns()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.removeListener()
        _binding = null
        myTreadHandler.handler?.removeCallbacksAndMessages(null)
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.apply {
                    mainFragmentLoadingLayout.visibility = View.GONE
                    mainFragmentRootView.showSnackBar(
                        getString(R.string.error),
                        getString(R.string.reload),
                        { viewModel.getWeatherFromLocalSourceRus() }
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myTreadHandler.start()
        checkingCitiesOnLoadOrAdd()
    }

    private fun checkingCitiesOnLoadOrAdd() {

        if (!preferenceFileExist(CITY_EXIST)) {
            activity?.let {
                with(
                    it.getSharedPreferences(CITY_EXIST, Context.MODE_PRIVATE)
                        .edit()
                ) {
                    putString(IS_CITY_KEY, EXIST_CITY)
                    apply()
                }
            }
//  скопировать из базы в Entity города после первого запуска после установки
            myTreadHandler.handler?.post {
                val cityList: MutableList<City> = gatherCities()
                for (city in cityList) {
                    viewModel.saveCityToEntity(city)
                }
            }
        } else {
// проверка на совпадение городов в Weather и в базе Entity - при добавлении города в приложении - заглушка
            activity?.let {
                with(
                    it.getSharedPreferences(CITY_EXIST, Context.MODE_PRIVATE)
                        .edit()
                ) {
                    putString(IS_CITY_KEY, ADD_CITY)
                    apply()
                }
            }
        }
/*
        myTreadHandler.handler?.post {
            val cityList: MutableList<City> = gatherCities()
            for (city in cityList) {
                viewModel.saveCityToEntity(city)
            }
        }
        */
    }

    private fun preferenceFileExist(fileName: String): Boolean {
        val filePath = File(
            context?.applicationInfo?.dataDir + SHARE_PREF
                    + fileName + SUFFIX_XML
        )
        return filePath.exists()
    }

    private fun gatherCities(): MutableList<City> {
        val cityList = mutableListOf<City>()
        for (cityItem in getWorldCities()) {
            cityList.add(cityItem.city)
        }
        for (cityItem in getRussianCities()) {
            cityList.add(cityItem.city)
        }
        return cityList
    }

    private fun showFavoriteCities(): Boolean? {
        val favoriteState = false
        return activity?.let {
            it.getSharedPreferences(FAVORITE_STATE, Context.MODE_PRIVATE)
                .getBoolean(IS_FAVORITE_STATE, favoriteState)
        }
    }

    // TODO GeoLocation
    private val onLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            getAddressAsync(location)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            super.onStatusChanged(provider, status, extras)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }
    }

    private fun checkPermission() {

        activity?.let {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PERMISSION_GRANTED -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRationale()
                }
                else -> {
                    myRequestPermission()
                }
            }
        }
    }

    private fun myRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            when {
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRationale()
                }
                else -> {
                    showDialog(
                        getString(R.string.dialog_title_no_gps),
                        getString(R.string.dialog_message_no_gps)
                    )
                }
            }
        }
    }

    private fun showRationale() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_title_no_gps))
            .setMessage(getString(R.string.need_permission_message))
            .setPositiveButton(getString(R.string.need_permission_ok)) { _, _ ->
                myRequestPermission()
            }
            .setNegativeButton(getString(R.string.need_permission_no)) { dialog, _ ->
                dialog.dismiss()
                requireActivity().finish()
            }
            .create()
            .show()
    }

    private fun getLocation() {
        activity?.let { context ->
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val providerGPS = locationManager.getProvider(LocationManager.GPS_PROVIDER)

                    providerGPS?.let {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            REFRESH_PERIOD,
                            MINIMAL_DISTANCE,
                            onLocationListener
                        )
                    }
                } else {
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                    if (location == null) {
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_location_unknown)
                        )

                    } else {
                        getAddressAsync(location)
                        showDialog( // TODO может showSettings()
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_known_location)
                        )
                    }
                }
            } else {
                showRationale()
            }
        }
    }

    private fun showDialog(title: String, message: String) {
        activity?.let {
            androidx.appcompat.app.AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun getAddressAsync(location: Location) {
        Thread {
            try {
                val geocoder = Geocoder(requireContext())
                val listAddress = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                binding.mainFragmentFABLocation.post {
                    showAddressDialog(listAddress[0].getAddressLine(0), location)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun showAddressDialog(address: String, location: Location) {
        // TODO проверка на пустой address???
        activity?.let {
            androidx.appcompat.app.AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    toDetailsWeather(Weather(
                        City(
                            address,
                            location.latitude,
                            location.longitude,
                            false,
                            "",
                            "RU"
                        )
                    ))
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun openDetailsFragment(weather: Weather) {
        toDetailsWeather(weather)
    }

    private fun toDetailsWeather(weather: Weather) {
        activity?.supportFragmentManager?.apply {
            beginTransaction()
                .add(
                    R.id.container,
                    DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                    })
                )
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }
}