package ru.gb.kotlinapp.view.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.gb.kotlinapp.R
import ru.gb.kotlinapp.databinding.FragmentWeatherCityBinding
import ru.gb.kotlinapp.model.Weather
import ru.gb.kotlinapp.model.WeatherDTO
import ru.gb.kotlinapp.util.longitudeSunDay
import ru.gb.kotlinapp.util.plusMinusTemperature
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment : Fragment(R.layout.main_fragment) {
    private var _binding: FragmentWeatherCityBinding? = null
    private val binding get() = _binding!!

    private lateinit var weatherBundle: Weather

    private val onLoadListener: WeatherLoader.WeatherLoaderListener = object : WeatherLoader.WeatherLoaderListener {
        override fun onLoaded(weatherDTO: WeatherDTO) {
            displayWeather(weatherDTO)
        }

        override fun onFailed(throwable: Throwable) {
           // TODO обработка ошибки
        }
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

        weatherBundle = arguments?.getParcelable<Weather>(BUNDLE_EXTRA)?:Weather()

        arguments?.getParcelable<Weather>(BUNDLE_EXTRA)?.let {
            it.city.also { city ->
                binding.cityName.text = city.city
            }
            with(binding){
                timeAfterLastUpdate.text = String.format(
                    getString(R.string.time_after_last_update),
                    "",
                    ""
                )
                currentTimeData.text = String.format(
                    getString(R.string.current_time_data),
                    SimpleDateFormat("HH:mm, EEEE, d MMMM").format(Calendar.getInstance().time)
                )
                temperatureValue.text = String.format(
                    getString(R.string.current_weather_value),
                    plusMinusTemperature(it.temperature),
                    it.weatherCondition
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
                    it.windDirection
                )
                cloudinessValue.text = String.format(
                    getString(R.string.cloudiness_value),
                    ((it.cloudiness * 100).toInt()).toString(),
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
                moonValue.text = it.moon
            }
/*
* imitations of time delay
* */
            object : CountDownTimer(2000, 500) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.loadingLayout.visibility = View.VISIBLE
                }

                override fun onFinish() {
                    binding.loadingLayout.visibility = View.GONE
                }
            }.start()
        }
    }

    private fun displayWeather(weatherDTO: WeatherDTO){

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