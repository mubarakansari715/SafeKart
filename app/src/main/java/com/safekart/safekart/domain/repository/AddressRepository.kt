package com.safekart.safekart.domain.repository

import com.safekart.safekart.data.model.Address
import kotlinx.coroutines.flow.StateFlow

interface AddressRepository {
    val addresses: StateFlow<List<Address>>
    val isLoading: StateFlow<Boolean>
    fun refresh()
    fun addAddress(address: Address)
    /** Saves address via API and returns when done. Use when UI needs to show saving state (e.g. button loader). */
    suspend fun addAddressAndWait(address: Address): Result<Unit>
    fun removeAddress(addressId: String)
    fun getById(addressId: String): Address?
}
