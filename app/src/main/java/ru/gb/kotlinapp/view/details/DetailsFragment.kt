package ru.gb.kotlinapp.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.gb.kotlinapp.R
import ru.gb.kotlinapp.databinding.FragmentWeatherCityBinding
import ru.gb.kotlinapp.model.Weather

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weather = arguments?.getParcelable<Weather>(BUNDLE_EXTRA)

        if (weather != null) {
            val city = weather.city
            binding.cityName.text = city.city
            binding.timeAfterLastUpdate.text = String.format(
                getString(R.string.time_after_last_update),
                "",
                ""
            )
            binding.currentTimeData.text = String.format(
                getString(R.string.current_time_data),
                "",
                "",
                ""
            )
            binding.temperatureValue.text = String.format(
                getString(R.string.current_weather_value),
                plusMinusTemperature(weather.temperature),
                ""
            )
            binding.feelsLikeValue.text = String.format(
                getString(R.string.feels_like_label_value),
                plusMinusTemperature(weather.feelsLike)
            )
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