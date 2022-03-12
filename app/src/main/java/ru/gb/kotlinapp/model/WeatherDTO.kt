package ru.gb.kotlinapp.model

data class WeatherDTO(
    val fact: FactDTO?
)

data class FactDTO(
    val temp: Int?,
    val feels_like: Int?,
    val condition: String?,
    val pressure_mm: Int?,
    val humidity: Int?
)