package ru.gb.kotlinapp.model.repository.room.city

import ru.gb.kotlinapp.model.City
import ru.gb.kotlinapp.model.Weather
import ru.gb.kotlinapp.model.room.history.HistoryDao
import ru.gb.kotlinapp.util.convertCityEntityToView
import ru.gb.kotlinapp.util.convertCityOfRegionToWeather
import ru.gb.kotlinapp.util.convertCityToEntity
import ru.gb.kotlinapp.util.convertCityToEntityInsert

class LocalRepoCityImpl(private val localCitySource: HistoryDao) : LocalRepoCity {

    override fun saveCity(city: City) {
        localCitySource.insertCity(
            convertCityToEntityInsert(city)
        )
    }

    override fun addCity(city: City) {
        val isCity = localCitySource.findNameCity(city.city)
        if (isCity == null) {
            localCitySource.addReplaceCity(convertCityToEntityInsert(city))
        }
    }

    override fun updateCityCondition(city: City) {
        val cityId = localCitySource.getNameCity(city.city)[0].id
        localCitySource.updateCity(
            convertCityToEntity(city, cityId)
        )
    }

    override fun getCityCondition(city: City): List<City> {
        val cityName = localCitySource.getNameCity(city.city)
        return convertCityEntityToView(cityName)
    }

    override fun getCityRegion(region: String): List<Weather> {
        val citiesOfRegion = convertCityEntityToView(localCitySource.getCitiesOnRegion(region))
        return convertCityOfRegionToWeather(citiesOfRegion)
    }

    override fun getCityRegionFavorite(region: String): List<Weather> {
        val citiesOfRegionFavorite =
            convertCityEntityToView(localCitySource.getCitiesRegionFavorite(region))
        return convertCityOfRegionToWeather(citiesOfRegionFavorite)
    }

    override fun getAllCity(): List<City> {
        TODO("Заглушка для добавления города")
    }
}