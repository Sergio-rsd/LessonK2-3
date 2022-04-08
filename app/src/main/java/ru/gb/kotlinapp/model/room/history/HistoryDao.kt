package ru.gb.kotlinapp.model.room.history

import androidx.room.*
import ru.gb.kotlinapp.model.room.city.CityEntity
import ru.gb.kotlinapp.model.room.city.CityWithHistory

@Dao
interface HistoryDao {
    @Transaction
    @Query("SELECT CityEntity.city, HistoryEntity.temperature, HistoryEntity.condition, CityEntity.favorite, CityEntity.note " + "FROM HistoryEntity, CityEntity " + "WHERE CityEntity.id == HistoryEntity.city_id")
    fun all(): List<CityWithHistory>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryEntity)

    @Query("SELECT * FROM CityEntity WHERE city LIKE :city")
    fun getNameCity(city: String): List<CityEntity>

    @Update
    fun update(entity: HistoryEntity)

    @Delete
    fun delete(entity: HistoryEntity)

    @Query("DELETE FROM HistoryEntity WHERE id = :id")
    fun deleteById(id: Long)

    // добавление нового города
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCity(entity: CityEntity)

    @Update
    fun updateCity(entity: CityEntity)
}