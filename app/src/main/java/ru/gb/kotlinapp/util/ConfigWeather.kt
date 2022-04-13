package ru.gb.kotlinapp.util

const val DEBUG_VALUE = false
const val BASE_URL = "https://api.weather.yandex.ru/"
const val HEADER_DETAIL_ICON = "https://freepngimg.com/thumb/city/36275-3-city-hd.png"
const val SERVER_ERROR = "Ошибка сервера"
const val REQUEST_ERROR = "Ошибка запроса на сервер"
const val CORRUPTED_DATA = "Неполные данные"

const val CURRENT_TIME_DATE = "HH:mm, EEEE, d MMMM"
const val TAG ="happy"
const val SHARE_PREF = "/shared_prefs/"
const val SUFFIX_XML = ".xml"
const val EXIST_CITY = "EXIST_CITY"
const val ADD_CITY = "CONTROL_CITY"
const val REGION_RU = "RU"
const val REGION_WORLD = "WORLD"
// зафиксировать выбор только любимых городов
const val FAVORITE_STATE = "FAVORITE_STATE"
const val IS_FAVORITE_STATE = "IS_FAVORITE_STATE"


data class ConfigWeather(
    val DEBUG: Boolean = true
)

enum class ConfigDebug(s: Boolean) {
    DEBUG(true),
    RELEASE(false)
}