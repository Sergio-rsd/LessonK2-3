package ru.gb.kotlinapp.viewmodel

import ru.gb.kotlinapp.model.Weather

sealed class AppState {
    data class Success(val weatherData: List<Weather>) : AppState()
// TODO city
    //    data class Success(val weatherData: List<Weather>, val cityData: List<City>) : AppState()
    data class Error(val error: Throwable?) : AppState()
    object Loading : AppState()
}
