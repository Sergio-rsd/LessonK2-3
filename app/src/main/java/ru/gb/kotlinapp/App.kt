package ru.gb.kotlinapp

import android.app.Application
import androidx.room.Room
import ru.gb.kotlinapp.model.room.HistoryDao
import ru.gb.kotlinapp.model.room.HistoryDataBase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "History.db"

        fun getHistoryDao(): HistoryDao {
            synchronized(HistoryDataBase::class.java) {
                if (db == null) {
                    if (appInstance == null) throw IllegalStateException("APP must not be null")
                    db = Room.databaseBuilder(
                        appInstance!!.applicationContext,
                        HistoryDataBase::class.java,
                        DB_NAME
                    )
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return db!!.historyDao()
        }
    }
}