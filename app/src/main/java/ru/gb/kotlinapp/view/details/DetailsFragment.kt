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
import ru.gb.kotlinapp.util.longitudeSunDay
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment : Fragment() {
    private var _binding: FragmentWeatherCityBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weather = arguments?.getParcelable<Weather>(BUNDLE_EXTRA)

        if (weather != null) {
            val city = weather.city
            object : CountDownTimer(2000, 500) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.loadingLayout.visibility = View.VISIBLE
                }

                override fun onFinish() {
                    binding.loadingLayout.visibility = View.GONE
                }
            }.start()

            binding.cityName.text = city.city
            binding.timeAfterLastUpdate.text = String.format(
                getString(R.string.time_after_last_update),
                "",
                ""
            )
            binding.currentTimeData.text = String.format(
                getString(R.string.current_time_data),
                SimpleDateFormat("HH:mm, EEEE, d MMMM").format(Calendar.getInstance().time),

            )
            binding.temperatureValue.text = String.format(
                getString(R.string.current_weather_value),
                plusMinusTemperature(weather.temperature),
                weather.weatherCondition
            )
            binding.feelsLikeValue.text = String.format(
                getString(R.string.feels_like_label_value),
                plusMinusTemperature(weather.feelsLike)
            )
            binding.precipitationStrengthValue.text = String.format(
                getString(R.string.precipitation_strength_value),
                weather.precipitationStrength.toString()
            )
            binding.pressureMmValue.text=String.format(
                getString(R.string.pressure_mm_value),
                    weather.pressure.toString(),
                    weather.pressureNormal.toString()
            )
            binding.humidityValue.text = String.format(
                getString(R.string.humidity_value),
                weather.humidity.toString(),
                '%'
            )
            binding.windValue.text = String.format(
                getString(R.string.wind_value),
                weather.wind.toString(),
                weather.windDirection
            )
            binding.cloudinessValue.text = String.format(
                getString(R.string.cloudiness_value),
                ((weather.cloudiness * 100).toInt()).toString(),
                '%'
            )
            binding.sunDayValue.text = String.format(
                getString(R.string.sun_day_value),
                weather.sunrise,
                weather.sunset
            )
            binding.longitudeDayValue.text = String.format(
                getString(R.string.longitude_day_value),
                longitudeSunDay(weather.sunrise,weather.sunset)
            )
            binding.moonValue.text = weather.moon
        }
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

    private fun plusMinusTemperature(temp: Int): String {
        return if (temp > 0) "+$temp" else temp.toString()
    }
}