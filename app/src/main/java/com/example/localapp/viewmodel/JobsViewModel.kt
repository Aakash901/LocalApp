package com.example.localapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.localapp.utility.JobsRepository
import com.example.localapp.database.JobDao
import com.example.localapp.database.JobDatabase
import com.example.localapp.database.ResultEntity
import com.example.localapp.database.toResultEntity
import com.example.localapp.model.ResultData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = JobsRepository()
    val jobs: LiveData<List<ResultData>> = repository.jobs
    val isLoading: LiveData<Boolean> = repository.isLoading

    private val jobDao: JobDao

    private val _bookmarkedJobs = MutableLiveData<List<ResultEntity>>()
    val bookmarkedJobs: LiveData<List<ResultEntity>> = _bookmarkedJobs
    private val _isLoadingBookmarks = MutableLiveData<Boolean>()
    val isLoadingBookmarks: LiveData<Boolean> = _isLoadingBookmarks

    init {
        loadMoreJobs()
        val database = JobDatabase.getDatabase(application)
        jobDao = database.jobDao()
    }


    fun loadBookmarkedJobs() {
        viewModelScope.launch {
            _isLoadingBookmarks.postValue(true)
            val savedJobs = withContext(Dispatchers.IO) {
                jobDao.getAllSavedJobs()
            }
            _bookmarkedJobs.postValue(savedJobs)
            _isLoadingBookmarks.postValue(false)
        }
    }

    fun removeBookmark(job: ResultEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                jobDao.deleteJob(job.id)
            }
            loadBookmarkedJobs()
        }
    }
    fun toggleBookmark(job: ResultData) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val isBookmarked = jobDao.isJobBookmarked(job.id)
                if (isBookmarked) {
                    jobDao.deleteJob(job.id)
                } else {
                    jobDao.insert(job.toResultEntity())
                }
            }
        }
    }

    fun loadMoreJobs() {
        viewModelScope.launch {
            repository.fetchJobs()
        }
    }

    suspend fun isJobBookmarked(jobId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            jobDao.isJobBookmarked(jobId)
        }
    }

    fun saveJob(job: ResultData) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                jobDao.insert(job.toResultEntity())
            }
        }
    }

    suspend fun getSavedJobs(): List<ResultEntity> {
        return withContext(Dispatchers.IO) {
            jobDao.getAllSavedJobs()
        }
    }
}