package com.example.android.trackmysleepquality.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SleepNight::class], version = 1, exportSchema = true)
abstract class SleepDatabase : RoomDatabase() {
    abstract val sleepDatabaseDao: SleepDatabaseDao

    companion object {
        /**
         *
        When a variable is declared as volatile,
        the Java compiler and runtime take special steps to ensure that the
        value of the variable is always up-to-date
        the @Volatile annotation is typically more efficient than using traditional synchronization mechanisms
        However, volatile variables are not a substitute for more complex synchronization mechanisms
        when you need to enforce a specific order of operations or coordination between threads*/
        @Volatile
        var INSTANCE: SleepDatabase? = null
        fun getInstance(context: Context): SleepDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SleepDatabase::class.java,
                        "sleep_history_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}