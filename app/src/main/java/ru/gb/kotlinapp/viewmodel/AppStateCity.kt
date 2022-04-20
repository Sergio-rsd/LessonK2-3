package ru.gb.kotlinapp.viewmodel

import ru.gb.kotlinapp.model.City
// TODO city
sealed class AppStateCity {
    data class SuccessCity(val cityData: List<City>) : AppStateCity()
    data class ErrorCity(val error: Throwable?) : AppStateCity()
    object LoadingCity : AppStateCity()
}