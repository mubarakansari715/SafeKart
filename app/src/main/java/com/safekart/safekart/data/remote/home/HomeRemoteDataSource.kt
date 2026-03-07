package com.safekart.safekart.data.remote.home

import com.safekart.safekart.data.model.HomeData
import com.safekart.safekart.data.remote.api.HomeApiService
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRemoteDataSource @Inject constructor(
    private val homeApiService: HomeApiService
) {

    suspend fun getHome(): Result<HomeData> {
        return try {
            val response = homeApiService.getHome()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to load home data"))
            }
        } catch (e: HttpException) {
            val message = when (e.code()) {
                500 -> "Server error. Please try again later"
                else -> "Failed to load home: ${e.message()}"
            }
            Result.failure(Exception(message))
        } catch (e: Exception) {
            Result.failure(
                Exception(
                    when {
                        e.message?.contains("Unable to resolve host", ignoreCase = true) == true ->
                            "Network error. Please check your connection"
                        e.message?.contains("Failed to connect", ignoreCase = true) == true ->
                            "Cannot connect to server. Please check if server is running"
                        e.message?.contains("timeout", ignoreCase = true) == true ->
                            "Connection timeout. Please try again"
                        else -> e.message ?: "Failed to load home"
                    }
                )
            )
        }
    }
}
