package com.example.android.trackmysleepquality.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SleepDatabaseDao {
    // Insert new record
    @Insert
    suspend fun insert(sleepNight: SleepNight)

    // Update existing record
    @Update
    suspend fun update(sleepNight: SleepNight)

    // Retrieve a specific record
    @Query(
        "SELECT * FROM daily_sleep_quality_table WHERE nightId = :id"
    )
    suspend fun get(id: Long): SleepNight?

    // Delete a specific record
    @Delete
    suspend fun delete(sleepNight: SleepNight)

    // Delete all records
    @Query("DELETE FROM daily_sleep_quality_table")
    suspend fun clear()

    // Retrieve last night
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    suspend fun getTonight(): SleepNight?

    // Retrieve last night
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNights(): LiveData<List<SleepNight>>
}