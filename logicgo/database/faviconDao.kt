package com.example.logicgo.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.xdownloader.database.Favicon

@Dao
interface faviconDao {
    @Query("SELECT * FROM Favicon")
    fun getAll(): List<Favicon>

    @Query("SELECT * FROM Favicon")
    fun getAllLive(): LiveData<List<Favicon>>

    @Query("SELECT * FROM Favicon WHERE id LIKE :id")
    suspend fun findById(id: Int): Favicon

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favicon: Favicon)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg favicon: Favicon)

    @Update
    suspend fun update(favicon: Favicon)

    @Delete
    suspend fun delete(favicon: Favicon)
}
