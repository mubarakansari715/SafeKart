package com.safekart.safekart.data.repository

import com.safekart.safekart.data.model.Address
import com.safekart.safekart.data.model.AddressDto
import com.safekart.safekart.data.model.SaveAddressRequest
import com.safekart.safekart.data.remote.customer.AddressRemoteDataSource
import com.safekart.safekart.domain.repository.AddressRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressRepositoryImpl @Inject constructor(
    private val remote: AddressRemoteDataSource
) : AddressRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _addresses = MutableStateFlow<List<Address>>(emptyList())
    override val addresses: StateFlow<List<Address>> = _addresses.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        scope.launch { refreshAddresses() }
    }

    override fun refresh() {
        // Only show loading when we have no cached data (initial load). Otherwise refresh in background to avoid jerk.
        val isInitialLoad = _addresses.value.isEmpty()
        if (isInitialLoad) _isLoading.value = true
        scope.launch { refreshAddresses(showLoading = isInitialLoad) }
    }

    private suspend fun refreshAddresses(showLoading: Boolean = true) {
        if (showLoading) _isLoading.value = true
        try {
            remote.getAddresses().onSuccess { dtos -> _addresses.value = dtos.map { it.toDomain() } }
        } finally {
            _isLoading.value = false
        }
    }

    override fun addAddress(address: Address) {
        // Optimistic update — show instantly before API responds
        _addresses.value = _addresses.value + address
        scope.launch {
            val request = SaveAddressRequest(
                fullName = address.fullName,
                phone = address.phone,
                line1 = address.street,
                city = address.city,
                state = address.state,
                postalCode = address.pincode,
                isDefault = address.isDefault
            )
            // Replace optimistic entry with server-confirmed entry (gets real server-generated id)
            remote.addAddress(request).onSuccess { refreshAddresses() }
                .onFailure { _addresses.value = _addresses.value.filter { it.id != address.id } }
        }
    }

    override suspend fun addAddressAndWait(address: Address): Result<Unit> {
        val request = SaveAddressRequest(
            fullName = address.fullName,
            phone = address.phone,
            line1 = address.street,
            city = address.city,
            state = address.state,
            postalCode = address.pincode,
            isDefault = address.isDefault
        )
        return remote.addAddress(request)
            .fold(
                onSuccess = { refreshAddresses(); Result.success(Unit) },
                onFailure = { Result.failure(it) }
            )
    }

    override fun removeAddress(addressId: String) {
        _addresses.value = _addresses.value.filter { it.id != addressId }
        scope.launch {
            remote.deleteAddress(addressId).onSuccess { refreshAddresses() }
        }
    }

    override fun getById(addressId: String): Address? =
        _addresses.value.find { it.id == addressId }

    private fun AddressDto.toDomain() = Address(
        id = id,
        fullName = fullName,
        phone = phone,
        street = line1,
        city = city,
        state = state,
        pincode = postalCode,
        isDefault = isDefault
    )
}
