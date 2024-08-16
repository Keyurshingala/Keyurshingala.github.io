package com.example.logicgo.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.logicgo.database.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM History")
    fun getAll(): List<History>

    @Query("SELECT * FROM History")
    fun getAllLive(): LiveData<List<History>>

    @Query("SELECT * FROM History WHERE id LIKE :id")
    suspend fun findById(id: Long): History

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: History)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg history: History)

    @Update
    suspend fun update(history: History)

    @Delete
    suspend fun delete(history: History)

    @Query("DELETE FROM History")
    suspend fun clear()
}
