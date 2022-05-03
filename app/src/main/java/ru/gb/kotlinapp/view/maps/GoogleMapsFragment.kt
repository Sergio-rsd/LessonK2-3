package ru.gb.kotlinapp.view.maps

import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import ru.gb.kotlinapp.R
import ru.gb.kotlinapp.databinding.FragmentGoogleMapsMainBinding
import ru.gb.kotlinapp.model.City
import ru.gb.kotlinapp.model.Weather
import ru.gb.kotlinapp.util.REGION_RU
import ru.gb.kotlinapp.util.REGION_WORLD
import ru.gb.kotlinapp.view.details.DetailsFragment
import java.io.IOException

class GoogleMapsFragment : Fragment() {

    private var _binding: FragmentGoogleMapsMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    private val markers = arrayListOf<Marker>()

    private lateinit var weatherBundle: Weather

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        val cityCoordination = LatLng(weatherBundle.city.lat, weatherBundle.city.lon)

        googleMap.addMarker(MarkerOptions().position(cityCoordination).title(""))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityCoordination, 8f))
        getAddressAsync(cityCoordination)

        googleMap.setOnMapLongClickListener {
            getAddressAsync(it)
            addMarkerOnMap(it)
            drawLine()
        }
        googleMap.setOnInfoWindowClickListener {
            getWeatherCity()
        }
        googleMap.uiSettings.isZoomControlsEnabled = true
    }

    private fun getWeatherCity() {
        val geocoder = Geocoder(requireContext())
        val listAddress = geocoder.getFromLocationName(binding.searchAddress.text.toString(), 1)
        val nameCityOnCoordination =
            geocoder.getFromLocation(listAddress[0].latitude, listAddress[0].longitude, 1)

        var nameCityReal = nameCityOnCoordination[0].locality
        if (nameCityReal == null) {
            nameCityReal = binding.searchAddress.text.toString()
        }

        val country = nameCityOnCoordination[0].countryName

        val region = if (country.equals("Россия")) {
            REGION_RU
        } else {
            REGION_WORLD
        }

        val weather = Weather(
            City(
                nameCityReal,
                nameCityOnCoordination[0].latitude,
                nameCityOnCoordination[0].longitude,
                false,
                "",
                region
            )
        )
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

    private fun drawLine() {
        val lastMarker = markers.size
        if (lastMarker > 1) {
            map.addPolyline(
                PolylineOptions()
                    .add(markers[lastMarker - 1].position, markers[lastMarker - 2].position)
                    .color(Color.RED)
                    .width(4f)
            )
        }
    }

    private fun addMarkerOnMap(location: LatLng) {
        val marker = map.addMarker(
            MarkerOptions().position(location)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
        )
        markers.add(marker)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoogleMapsMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        weatherBundle = arguments?.getParcelable(BUNDLE_MAP) ?: Weather()

        binding.buttonSearch.setOnClickListener {
            searchPlace()
        }
    }

    private fun searchPlace() {
        Thread {
            try {
                val geocoder = Geocoder(requireContext())
                val listAddress =
                    geocoder.getFromLocationName(binding.searchAddress.text.toString(), 1)
                requireActivity().runOnUiThread {
                    if (listAddress.isEmpty()) {
                        activity?.let {
                            androidx.appcompat.app.AlertDialog.Builder(it)
                                .setTitle(binding.searchAddress.text.toString())
                                .setMessage(R.string.dialog_message_no_search_city)
                                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                                .create()
                                .show()
                        }
                    } else {
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    listAddress[0].latitude,
                                    listAddress[0].longitude
                                ), 9f
                            )
                        )
                        map.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    listAddress[0].latitude,
                                    listAddress[0].longitude
                                )
                            )
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin))
                                .title(getString(R.string.info_weather_on_search_city))
                        )
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun getAddressAsync(location: LatLng) {
        Thread {
            try {
                val geocoder = Geocoder(requireContext())
                val listAddress = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                requireActivity().runOnUiThread {
                    binding.textAddress.text = listAddress[0].getAddressLine(0)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    companion object {
        const val BUNDLE_MAP = "map"

        fun newInstance(bundle: Bundle): GoogleMapsFragment {
            val fragment = GoogleMapsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}