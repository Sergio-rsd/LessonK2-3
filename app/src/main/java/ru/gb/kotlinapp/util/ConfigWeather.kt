package ru.gb.kotlinapp.util

const val DEBUG_VALUE = false
const val BASE_URL = "https://api.weather.yandex.ru/"
const val SERVER_ERROR = "Ошибка сервера"
const val REQUEST_ERROR = "Ошибка запроса на сервер"
const val CORRUPTED_DATA = "Неполные данные"

const val CURRENT_TIME_DATE = "HH:mm, EEEE, d MMMM"


data class ConfigWeather(
    val DEBUG: Boolean = true
)

enum class ConfigDebug(s: Boolean) {
    DEBUG(true),
    RELEASE(false)
}