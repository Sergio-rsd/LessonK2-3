package ru.gb.kotlinapp.model.repository.room.city

import ru.gb.kotlinapp.model.City
import ru.gb.kotlinapp.model.room.history.HistoryDao
import ru.gb.kotlinapp.util.convertCityToEntity

class LocalRepoCityImpl(private val localCitySource: HistoryDao) : LocalRepoCity {
    override fun saveCity(city: City) {
        localCitySource.insertCity(
            convertCityToEntity(city)
        )
    }

    override fun getAllCity(): List<City> {
        TODO("Not yet implemented")
    }
}