package ru.gb.kotlinapp.model.room.city

data class CityWithHistory(
//    @ColumnInfo(name = "city_name")
    val city: String = "",
    val temperature: Int = 0,
    val condition: String = ""
    /*
    @ColumnInfo(name = "history_id")
    val id: Long = 0,
    @ColumnInfo(name = "city_history_id")
    val cityHistoryId : Long = 0,
    @ColumnInfo(name = "city_id")
    val cityId : Long = 0,
    @ColumnInfo(name = "favorite")
    val favorite : Int = 0
*/
    /*
    @Embedded
    val city: CityEntity,
    @Relation(
        parentColumn = "cityId",
        entityColumn = "cityCreateId"
    )
//    val historyList: List<HistoryEntity>
    val historyList: List<HistoryEntity>
*/
)