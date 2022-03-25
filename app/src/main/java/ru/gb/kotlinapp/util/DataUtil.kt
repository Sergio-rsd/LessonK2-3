package ru.gb.kotlinapp.util

import ru.gb.kotlinapp.model.*

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact = weatherDTO.fact!!
    val forecasts = weatherDTO.forecasts[0]!!
    val info = weatherDTO.info!!
    val partDayPressure = weatherDTO.forecasts[0]!!.parts!!.day!!
    return listOf(Weather((getDefaultCity()), fact.temp!!, fact.feels_like!!, fact.condition!!))
}

fun checkDTOtoNull(serverResponse: WeatherDTO): Boolean {
    val fact = serverResponse.fact
    val forecasts = serverResponse.forecasts[0]
    val info = serverResponse.info
    val partDayPressure = serverResponse.forecasts[0]?.parts?.day

    return (fact?.temp == null
            || fact.feels_like == null
            || fact.condition.isNullOrEmpty()
            || fact.cloudness == null
            || fact.pressure_mm == null
            || fact.humidity == null
            || fact.wind_speed == null
            || fact.wind_dir.isNullOrEmpty()
            || forecasts?.sunrise.isNullOrEmpty()
            || forecasts?.sunset.isNullOrEmpty()
            || forecasts?.moon_text.isNullOrEmpty()
            || info?.def_pressure_mm == null
            || partDayPressure?.prec_mm == null)
}
