package ru.gb.kotlinapp.model.repository.room.city

import ru.gb.kotlinapp.model.City
import ru.gb.kotlinapp.model.Weather

interface LocalRepoCity {
    fun saveCity(city: City)
    fun addCity(city: City)
    fun getAllCity(): List<City>
    fun updateCityCondition(city: City)
    fun getCityCondition(city: City): List<City>
    fun getCityRegion(region: String): List<Weather>
    fun getCityRegionFavorite(region: String): List<Weather>
}