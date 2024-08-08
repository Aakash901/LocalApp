package com.example.localapp.utility

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.localapp.model.ResultData

class JobsRepository {
    private val _jobs = MutableLiveData<List<ResultData>>()
    val jobs: LiveData<List<ResultData>> = _jobs

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var currentPage = 1



    suspend fun fetchJobs() {
        _isLoading.postValue(true)
        try {
            val response = JobsApi.service.getJobs(currentPage)
            val currentJobs = _jobs.value ?: emptyList()

            _jobs.postValue(currentJobs + response.results)
            Log.d("JobsRepository", "Total jobs after update: ${_jobs.value?.size}")

            currentPage++
        } catch (e: Exception) {
            Log.e("JobsRepository", "Error fetching jobs", e)
        }
        _isLoading.postValue(false)
    }
}
