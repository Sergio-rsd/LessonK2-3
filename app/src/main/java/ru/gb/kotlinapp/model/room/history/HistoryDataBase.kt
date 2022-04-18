package ru.gb.kotlinapp.model.room.history

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.gb.kotlinapp.model.room.city.CityEntity

@Database(entities = [HistoryEntity::class, CityEntity::class],  version = 1, exportSchema = false)
abstract class HistoryDataBase : RoomDatabase() {
    abstract fun historyDao() : HistoryDao
}