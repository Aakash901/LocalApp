package com.example.localapp.utility

import com.example.localapp.model.ResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface JobsApiService {
    @GET("common/jobs")
    suspend fun getJobs(@Query("page") page: Int): ResponseModel
}
