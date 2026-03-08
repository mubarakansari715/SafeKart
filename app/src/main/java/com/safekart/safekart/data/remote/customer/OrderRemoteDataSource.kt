package com.safekart.safekart.data.remote.customer

import com.safekart.safekart.data.model.OrderDto
import com.safekart.safekart.data.model.PlaceOrderRequest
import com.safekart.safekart.data.remote.api.CustomerApiService
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRemoteDataSource @Inject constructor(
    private val api: CustomerApiService
) {
    suspend fun getOrders(): Result<List<OrderDto>> = safeCall {
        val res = api.getOrders()
        if (res.success) Result.success(res.data ?: emptyList())
        else Result.failure(Exception(res.message ?: "Failed to fetch orders"))
    }

    suspend fun placeOrder(request: PlaceOrderRequest): Result<OrderDto> = safeCall {
        val res = api.placeOrder(request)
        if (res.success && res.data != null) Result.success(res.data)
        else Result.failure(Exception(res.message ?: "Failed to place order"))
    }

    private suspend fun <T> safeCall(block: suspend () -> Result<T>): Result<T> {
        return try { block() } catch (e: HttpException) {
            Result.failure(Exception(when (e.code()) { 401 -> "Session expired."; 402, 422 -> "Order validation failed."; else -> "Server error (${e.code()})" }))
        } catch (e: Exception) {
            Result.failure(Exception(when {
                e.message?.contains("Unable to resolve host", true) == true -> "Network error."
                e.message?.contains("Failed to connect", true) == true -> "Cannot connect to server."
                e.message?.contains("timeout", true) == true -> "Timeout. Try again."
                else -> e.message ?: "Unknown error"
            }))
        }
    }
}
