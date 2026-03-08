package com.safekart.safekart.data.remote.customer

import com.safekart.safekart.data.model.AddressDto
import com.safekart.safekart.data.model.SaveAddressRequest
import com.safekart.safekart.data.remote.api.CustomerApiService
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressRemoteDataSource @Inject constructor(
    private val api: CustomerApiService
) {
    suspend fun getAddresses(): Result<List<AddressDto>> = safeCall {
        val res = api.getAddresses()
        if (res.success) Result.success(res.data ?: emptyList())
        else Result.failure(Exception(res.message ?: "Failed to fetch addresses"))
    }

    suspend fun addAddress(request: SaveAddressRequest): Result<AddressDto> = safeCall {
        val res = api.addAddress(request)
        if (res.success && res.data != null) Result.success(res.data)
        else Result.failure(Exception(res.message ?: "Failed to save address"))
    }

    suspend fun deleteAddress(id: String): Result<Unit> = safeCall {
        val res = api.deleteAddress(id)
        if (res.success) Result.success(Unit)
        else Result.failure(Exception(res.message ?: "Failed to delete address"))
    }

    private suspend fun <T> safeCall(block: suspend () -> Result<T>): Result<T> {
        return try { block() } catch (e: HttpException) {
            Result.failure(Exception(when (e.code()) { 401 -> "Session expired."; else -> "Server error (${e.code()})" }))
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
