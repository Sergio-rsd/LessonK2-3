package ru.gb.kotlinapp.view.details

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.gb.kotlinapp.R
import ru.gb.kotlinapp.databinding.FragmentWeatherCityBinding
import ru.gb.kotlinapp.model.*
import ru.gb.kotlinapp.util.cloudPercent
import ru.gb.kotlinapp.util.longitudeSunDay
import ru.gb.kotlinapp.util.plusMinusTemperature
import ru.gb.kotlinapp.util.showSnackBarNoAction
import java.text.SimpleDateFormat
import java.util.*

const val CURRENT_TIME_DATE = "HH:mm, EEEE, d MMMM"
const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_TEMP_EXTRA = "TEMPERATURE"
const val DETAILS_FEELS_LIKE_EXTRA = "FEELS LIKE"
const val DETAILS_CONDITION_EXTRA = "CONDITION"
private const val TEMP_INVALID = -100
private const val FEELS_LIKE_INVALID = -100
private const val PROCESS_ERROR = "Обработка ошибки"

class DetailsFragment : Fragment(R.layout.main_fragment) {
    private var _binding: FragmentWeatherCityBinding? = null
    private val binding get() = _binding!!

    private lateinit var weatherBundle: Weather

    private val loadResultReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_INTENT_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_DATA_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_URL_MALFORMED_EXTRA -> TODO(PROCESS_ERROR)

                DETAILS_RESPONSE_SUCCESS_EXTRA -> renderData(
                    WeatherDTO(
                        FactDTO(
                            intent.getIntExtra(
                                DETAILS_TEMP_EXTRA, TEMP_INVALID
                            ),
                            intent.getIntExtra(DETAILS_FEELS_LIKE_EXTRA, FEELS_LIKE_INVALID),
                            intent.getStringExtra(DETAILS_CONDITION_EXTRA),
                            null, null, null, null, null
                        ), emptyList(), null
                    )
                )
            }
        }

    }

    private val onLoadListener: WeatherLoader.WeatherLoaderListener =
        object : WeatherLoader.WeatherLoaderListener {
            override fun onLoaded(weatherDTO: WeatherDTO) {
                displayWeather(weatherDTO)
            }

            override fun onFailed(throwable: Throwable) {
                binding.mainViewWeather.showSnackBarNoAction(
                    throwable.toString(),
                    ""
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(loadResultReceiver, IntentFilter(DETAILS_INTENT_FILTER))
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

        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()

/*
        binding.mainViewWeather.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE

        val loader = WeatherLoader(onLoadListener, weatherBundle.city.lat, weatherBundle.city.lon)
        loader.loadWeather()
*/
        getWeather()
    }

    private fun getWeather() {
        binding.mainViewWeather.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        context?.let {
            it.startService(Intent(it, DetailsService::class.java).apply {
                putExtra(LATITUDE_EXTRA, weatherBundle.city.lat)
                putExtra(LONGITUDE_EXTRA, weatherBundle.city.lon)
            })
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun renderData(weatherDTO: WeatherDTO) {
        with(binding) {
            mainViewWeather.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE
            val city = weatherBundle.city
            cityName.text = city.city

            currentTimeData.text = String.format(
                getString(R.string.current_time_data),
                SimpleDateFormat(CURRENT_TIME_DATE).format(Calendar.getInstance().time)
            )
            weatherDTO.fact?.let {
                temperatureValue.text = String.format(
                    getString(R.string.current_weather_value),
                    plusMinusTemperature(it.temp),
                    getCondition()[it.condition]
                )
                feelsLikeValue.text = String.format(
                    getString(R.string.feels_like_label_value),
                    plusMinusTemperature(it.feels_like)
                )
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun displayWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            mainViewWeather.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE
            val city = weatherBundle.city
            cityName.text = city.city

            currentTimeData.text = String.format(
                getString(R.string.current_time_data),
                SimpleDateFormat(CURRENT_TIME_DATE).format(Calendar.getInstance().time)
            )
            weatherDTO.fact?.let {
                temperatureValue.text = String.format(
                    getString(R.string.current_weather_value),
                    plusMinusTemperature(it.temp),
                    getCondition()[it.condition]
                )
                feelsLikeValue.text = String.format(
                    getString(R.string.feels_like_label_value),
                    plusMinusTemperature(it.feels_like)
                )
                pressureMmValue.text = String.format(
                    getString(R.string.pressure_mm_value),
                    it.pressure_mm.toString(),
                    weatherDTO.info?.def_pressure_mm.toString()
                )
                humidityValue.text = String.format(
                    getString(R.string.humidity_value),
                    it.humidity.toString(),
                    '%'
                )
                windValue.text = String.format(
                    getString(R.string.wind_value),
                    it.wind_speed.toString(),
                    getWindDirection()[it.wind_dir]
                )
                cloudinessValue.text = String.format(
                    getString(R.string.cloudiness_value),
                    cloudPercent(it.cloudness),
                    '%'
                )
            }

            weatherDTO.forecasts[0]?.let {
                sunDayValue.text = String.format(
                    getString(R.string.sun_day_value),
                    it.sunrise,
                    it.sunset
                )
                longitudeDayValue.text = String.format(
                    getString(R.string.longitude_day_value),
                    longitudeSunDay(it.sunrise, it.sunset)
                )
                precipitationStrengthValue.text = String.format(
                    getString(R.string.precipitation_strength_value),
                    it.parts?.day?.prec_mm.toString()
                )
                moonValue.text = getMoonCondition()[it.moon_text]
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultReceiver)
        }
        super.onDestroy()
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