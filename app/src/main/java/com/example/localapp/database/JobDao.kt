package com.example.localapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface JobDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(job: ResultEntity)

    @Query("SELECT * FROM results")
     fun getAllSavedJobs(): List<ResultEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM results WHERE id = :jobId LIMIT 1)")
     fun isJobBookmarked(jobId: Int): Boolean

    @Query("DELETE FROM results WHERE id = :jobId")
     fun deleteJob(jobId: Int)

}
