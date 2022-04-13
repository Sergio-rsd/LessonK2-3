package ru.gb.kotlinapp.util

import ru.gb.kotlinapp.model.City
import ru.gb.kotlinapp.model.Weather
import ru.gb.kotlinapp.model.WeatherDTO
import ru.gb.kotlinapp.model.getDefaultCity
import ru.gb.kotlinapp.model.room.city.CityEntity
import ru.gb.kotlinapp.model.room.city.CityWithHistory
import ru.gb.kotlinapp.model.room.history.HistoryEntity

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact = weatherDTO.fact!!
    val forecasts = weatherDTO.forecasts[0]!!
    val info = weatherDTO.info!!
    val partDayPressure = weatherDTO.forecasts[0]!!.parts!!.day!!
    return listOf(
        Weather(
            (getDefaultCity()),
            fact.temp!!,
            fact.feels_like!!,
            fact.condition!!,
            partDayPressure.prec_mm!!,
            fact.pressure_mm!!,
            info.def_pressure_mm!!,
            fact.humidity!!,
            fact.wind_speed!!,
            fact.wind_dir!!,
            fact.cloudness!!,
            forecasts.sunrise!!,
            forecasts.sunset!!,
            forecasts.moon_text!!
        )
    )
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

fun convertHistoryEntityToWeather(entityList: List<CityWithHistory>): List<Weather> {
    return entityList.map {
        Weather(
            City(it.city, 0.0, 0.0, it.favorite, it.note, it.region),
            it.temperature,
            0,
            it.condition
        )
    }
}

fun convertCityOfRegionToWeather(cityRegion: List<City>): List<Weather> {
    return cityRegion.map {
        Weather(City(it.city, it.lat, it.lon, it.favorite, it.note, it.region),0,0,"")
    }
}

fun convertWeatherToEntity(weather: Weather, city: List<CityEntity>): HistoryEntity {
    return HistoryEntity(0, city[0].id, weather.temperature, weather.weatherCondition)
}

fun convertCityEntityToView(entityCity: List<CityEntity>): List<City> {
    return entityCity.map {
        City(it.city, it.lat, it.lon, it.favorite, it.note, it.region)
    }
}

fun convertCityToEntity(city: City, cityId: Long): CityEntity {
    return CityEntity(cityId, city.city, city.lat, city.lon, city.favorite, city.note, city.region)
}

fun convertCityToEntityInsert(city: City): CityEntity {
    return CityEntity(0, city.city, city.lat, city.lon, city.favorite, city.note, city.region)
}