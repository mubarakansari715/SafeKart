package com.safekart.safekart.data.remote.customer

import com.safekart.safekart.data.model.AddCartItemRequest
import com.safekart.safekart.data.model.CartItemSnapshot
import com.safekart.safekart.data.model.UpdateCartItemRequest
import com.safekart.safekart.data.remote.api.CustomerApiService
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRemoteDataSource @Inject constructor(
    private val api: CustomerApiService
) {
    suspend fun getCart(): Result<List<CartItemSnapshot>> = safeCall {
        val res = api.getCart()
        if (res.success) Result.success(res.data?.items ?: emptyList())
        else Result.failure(Exception(res.message ?: "Failed to fetch cart"))
    }

    suspend fun addItem(request: AddCartItemRequest): Result<List<CartItemSnapshot>> = safeCall {
        val res = api.addToCart(request)
        if (res.success) Result.success(res.data?.items ?: emptyList())
        else Result.failure(Exception(res.message ?: "Failed to add item"))
    }

    suspend fun updateItem(productId: String, quantity: Int): Result<List<CartItemSnapshot>> = safeCall {
        val res = api.updateCartItem(UpdateCartItemRequest(productId, quantity))
        if (res.success) Result.success(res.data?.items ?: emptyList())
        else Result.failure(Exception(res.message ?: "Failed to update item"))
    }

    suspend fun removeItem(productId: String): Result<List<CartItemSnapshot>> = safeCall {
        val res = api.removeCartItem(productId)
        if (res.success) Result.success(res.data?.items ?: emptyList())
        else Result.failure(Exception(res.message ?: "Failed to remove item"))
    }

    private suspend fun <T> safeCall(block: suspend () -> Result<T>): Result<T> {
        return try {
            block()
        } catch (e: HttpException) {
            Result.failure(Exception(when (e.code()) {
                401 -> "Session expired. Please login again."
                else -> "Server error (${e.code()})"
            }))
        } catch (e: Exception) {
            Result.failure(Exception(networkErrorMessage(e)))
        }
    }

    private fun networkErrorMessage(e: Exception) = when {
        e.message?.contains("Unable to resolve host", true) == true -> "Network error. Check your connection."
        e.message?.contains("Failed to connect", true) == true -> "Cannot connect to server."
        e.message?.contains("timeout", true) == true -> "Connection timeout. Please try again."
        else -> e.message ?: "Unknown error"
    }
}
