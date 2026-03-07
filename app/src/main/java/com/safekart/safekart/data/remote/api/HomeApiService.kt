package com.safekart.safekart.data.remote.api

import com.safekart.safekart.data.model.HomeApiResponse
import retrofit2.http.GET

/**
 * API service for home screen – GET /api/v1/home
 */
interface HomeApiService {

    @GET("home")
    suspend fun getHome(): HomeApiResponse
}
