package ru.gb.kotlinapp.model

import java.util.*

data class WeatherDTO(
    val fact: FactDTO?,
    val forecasts: List<ForecastsDTO?> = LinkedList<ForecastsDTO?>(),
    val info: InfoDTO?
)

data class FactDTO(
    val temp: Int?,
    val feels_like: Int?,
    val condition: String?,
    val cloudness: Double?,
    val pressure_mm: Int?,
    val humidity: Int?,
    val wind_speed: Double?,
    val wind_dir: String?
)

data class ForecastsDTO(
    val sunrise: String?,
    val sunset: String?,
    val moon_text: String,
    val parts: ForecastsPartDTO?,
)

data class ForecastsPartDTO(
    val day: ForecastsDayDTO?
)

data class ForecastsDayDTO(
    val prec_mm: Double?
)

data class InfoDTO(
    val def_pressure_mm: Int?
)