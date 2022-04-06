package ru.gb.kotlinapp.model.room.history

import androidx.room.*
import ru.gb.kotlinapp.model.room.city.CityEntity
import ru.gb.kotlinapp.model.room.city.CityWithHistory

@Dao
interface HistoryDao {
    /*
            @Query("SELECT * FROM HistoryEntity")
        fun all(): List<HistoryEntity>
        */
//    , CityEntity.cityId AS CityEntity.city_name
    @Transaction
//@Query("SELECT * FROM HistoryEntity, CityEntity")
//    @Query("SELECT HistoryEntity.temperature, HistoryEntity.condition, HistoryEntity.city_id, CityEntity.id, CityEntity.city AS city_name, CityEntity.favorite " + "FROM HistoryEntity, CityEntity " + "WHERE CityEntity.id == HistoryEntity.city_id")
//    @Query("SELECT * " + "FROM HistoryEntity, CityEntity " + "WHERE CityEntity.id == HistoryEntity.city_id")
    @Query("SELECT CityEntity.city, HistoryEntity.temperature, HistoryEntity.condition " + "FROM HistoryEntity, CityEntity " + "WHERE CityEntity.id == HistoryEntity.city_id")
    fun all(): List<CityWithHistory>

/*    @Query("SELECT * FROM HistoryEntity WHERE city LIKE :city")
    fun getDataByTown(city: String): List<HistoryEntity>
    */

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
}