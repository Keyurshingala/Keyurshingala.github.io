package com.example.logicgo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xdownloader.model.BrowserTab
import com.xdownloader.model.Favicon

@Database(
        entities = [History::class, BrowserTab::class, Favicon::class],
        version = 1,
        exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun tabDao(): TabDao
    abstract fun faviconDao(): faviconDao
}
