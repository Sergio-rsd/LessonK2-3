package ru.gb.kotlinapp.model.repository.room.city

import ru.gb.kotlinapp.model.City

interface LocalRepoCity {
    fun saveCity(city: City)
    fun getAllCity() : List<City>
    fun updateCityCondition(city: City)
    fun getCityCondition(city: City): List<City>
}