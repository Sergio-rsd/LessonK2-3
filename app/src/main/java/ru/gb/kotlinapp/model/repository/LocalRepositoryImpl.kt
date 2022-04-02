package ru.gb.kotlinapp.model.repository

import ru.gb.kotlinapp.model.Weather
import ru.gb.kotlinapp.model.room.HistoryDao
import ru.gb.kotlinapp.util.convertHistoryEntityToWeather
import ru.gb.kotlinapp.util.convertWeatherToEntity

class LocalRepositoryImpl(private val localDataSource: HistoryDao) : LocalRepository {
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToEntity(weather))
    }
}