package ru.gb.kotlinapp.util

const val DEBUG_VALUE = true
const val BASE_URL = "https://api.weather.yandex.ru/"
const val SERVER_ERROR = "Ошибка сервера"
const val REQUEST_ERROR = "Ошибка запроса на сервер"
const val CORRUPTED_DATA = "Неполные данные"

data class ConfigWeather(
    val DEBUG: Boolean = true
)

enum class ConfigDebug(s: Boolean) {
    DEBUG(true),
    RELEASE(false)
}