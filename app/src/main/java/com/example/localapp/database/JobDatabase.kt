package com.example.localapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ResultEntity::class], version = 1)
abstract class JobDatabase : RoomDatabase() {
    abstract fun jobDao(): JobDao

    companion object {
        @Volatile
        private var INSTANCE: JobDatabase? = null

        fun getDatabase(context: Context): JobDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JobDatabase::class.java,
                    "job_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}