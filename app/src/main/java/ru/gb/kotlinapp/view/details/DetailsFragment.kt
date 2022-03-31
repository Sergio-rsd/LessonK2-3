package ru.gb.kotlinapp.view.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import ru.gb.kotlinapp.R
import ru.gb.kotlinapp.databinding.FragmentWeatherCityBinding
import ru.gb.kotlinapp.model.Weather
import ru.gb.kotlinapp.model.getCondition
import ru.gb.kotlinapp.model.getMoonCondition
import ru.gb.kotlinapp.model.getWindDirection
import ru.gb.kotlinapp.util.*
import ru.gb.kotlinapp.viewmodel.AppState
import ru.gb.kotlinapp.viewmodel.DetailsViewModel
import java.text.SimpleDateFormat
import java.util.*


class DetailsFragment : Fragment(R.layout.main_fragment) {
    private var _binding: FragmentWeatherCityBinding? = null
    private val binding get() = _binding!!

    private lateinit var weatherBundle: Weather

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this)[DetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()

        viewModel.detailsLiveData.observe(viewLifecycleOwner) { renderDataWeather(it) }

        requestWeather()
    }

    private fun renderDataWeather(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                with(binding) {
                    mainViewWeather.visibility = View.VISIBLE
                    loadingLayout.visibility = View.GONE
                }
                setWeather(appState.weatherData[0])
            }
            is AppState.Loading -> {
                with(binding) {
                    mainViewWeather.visibility = View.GONE
                    loadingLayout.visibility = View.VISIBLE
                }
            }
            is AppState.Error -> {
                with(binding) {
                    mainViewWeather.visibility = View.VISIBLE
                    loadingLayout.visibility = View.GONE
                    mainViewWeather.showSnackBar(
                        getString(R.string.error),
                        getString(R.string.reload),
                        { requestWeather() }
                    )
                }
            }
        }
    }

    private fun requestWeather() {
        viewModel.getWeatherFromRemoteSource(weatherBundle.city.lat, weatherBundle.city.lon)
    }

    @SuppressLint("SimpleDateFormat")
    private fun setWeather(weather: Weather) {
        val city = weatherBundle.city
        with(binding) {
            cityName.text = city.city

            currentTimeData.text = String.format(
                getString(R.string.current_time_data),
                SimpleDateFormat(CURRENT_TIME_DATE).format(Calendar.getInstance().time)
            )
            weather.let {
                temperatureValue.text = String.format(
                    getString(R.string.current_weather_value),
                    plusMinusTemperature(it.temperature),
                    getCondition()[it.weatherCondition]
                )
                feelsLikeValue.text = String.format(
                    getString(R.string.feels_like_label_value),
                    plusMinusTemperature(it.feelsLike)
                )
                precipitationStrengthValue.text = String.format(
                    getString(R.string.precipitation_strength_value),
                    it.precipitationStrength.toString()
                )
                pressureMmValue.text = String.format(
                    getString(R.string.pressure_mm_value),
                    it.pressure.toString(),
                    it.pressureNormal.toString()
                )
                humidityValue.text = String.format(
                    getString(R.string.humidity_value),
                    it.humidity.toString(),
                    '%'
                )
                windValue.text = String.format(
                    getString(R.string.wind_value),
                    it.wind.toString(),
                    getWindDirection()[it.windDirection]
                )
                cloudinessValue.text = String.format(
                    getString(R.string.cloudiness_value),
                    cloudPercent(it.cloudiness),
                    '%'
                )
                sunDayValue.text = String.format(
                    getString(R.string.sun_day_value),
                    it.sunrise,
                    it.sunset
                )
                longitudeDayValue.text = String.format(
                    getString(R.string.longitude_day_value),
                    longitudeSunDay(it.sunrise, it.sunset)
                )
                moonValue.text = getMoonCondition()[it.moon]
            }
        }
        binding.headerIcon.load(HEADER_DETAIL_ICON)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}