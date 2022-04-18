package ru.gb.kotlinapp.viewmodel

import ru.gb.kotlinapp.model.Weather

sealed class AppState {
    // TODO thread
    data class Success(val weatherData: List<Weather>) : AppState()
//    data class Success(val weatherData: Any) : AppState()
    data class Error(val error: Throwable?) : AppState()
    object Loading : AppState()
}
