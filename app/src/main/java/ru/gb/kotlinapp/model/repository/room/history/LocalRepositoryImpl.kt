package ru.gb.kotlinapp.model.repository.room.history

import ru.gb.kotlinapp.model.Weather
import ru.gb.kotlinapp.model.room.history.HistoryDao
import ru.gb.kotlinapp.util.convertHistoryEntityToWeather
import ru.gb.kotlinapp.util.convertWeatherToEntity

class LocalRepositoryImpl(private val localDataSource: HistoryDao) : LocalRepository {
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        val city = localDataSource.getNameCity(weather.city.city)
        localDataSource.insert(
            convertWeatherToEntity(weather, city)
        )
    }
}