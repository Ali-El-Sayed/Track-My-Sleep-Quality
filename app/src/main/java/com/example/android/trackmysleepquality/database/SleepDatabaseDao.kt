package com.example.android.trackmysleepquality.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SleepDatabaseDao {
    // Insert new record
    @Insert
    fun insert(sleepNight: SleepNight)

    // Update existing record
    @Update
    fun update(sleepNight: SleepNight)

    // Retrieve a specific record
    @Query(
        "SELECT * FROM daily_sleep_quality_table WHERE nightId = :id"
    )
    fun get(id: Long): SleepNight?

    // Delete a specific record
    @Delete
    fun delete(sleepNight: SleepNight)

    // Delete all records
    @Query("DELETE FROM daily_sleep_quality_table")
    fun clear()

    // Retrieve last night
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    fun getTonight(): SleepNight?

    // Retrieve last night
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNights(): LiveData<List<SleepNight>>
}