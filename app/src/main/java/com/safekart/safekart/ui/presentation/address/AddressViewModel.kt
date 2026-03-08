package com.safekart.safekart.ui.presentation.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safekart.safekart.data.model.Address
import com.safekart.safekart.domain.repository.AddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val addressRepository: AddressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState: StateFlow<AddressUiState> = _uiState.asStateFlow()

    init {
        // Refresh addresses when entering screen (e.g. after app reopen or from cart)
        addressRepository.refresh()
        // Reactively collect loading state; when load finishes, show add form if empty or select default address
        viewModelScope.launch {
            addressRepository.isLoading.collect { loading ->
                _uiState.update { current ->
                    var next = current.copy(isLoading = loading)
                    if (!loading) {
                        val list = addressRepository.addresses.value
                        if (list.isEmpty()) {
                            next = next.copy(showAddForm = true, selectedAddressId = null)
                        } else if (next.selectedAddressId == null || list.none { it.id == next.selectedAddressId }) {
                            next = next.copy(selectedAddressId = list.find { it.isDefault }?.id ?: list.first().id)
                        }
                    }
                    next
                }
            }
        }
        // Reactively collect address list — updates whenever API responds
        viewModelScope.launch {
            addressRepository.addresses.collect { list ->
                _uiState.update { current ->
                    var next = current.copy(addresses = list)
                    // Keep selection valid when list changes (e.g. after remove)
                    if (list.isNotEmpty() && (current.selectedAddressId == null || list.none { it.id == current.selectedAddressId })) {
                        next = next.copy(selectedAddressId = list.find { it.isDefault }?.id ?: list.first().id)
                    }
                    if (list.isEmpty()) next = next.copy(selectedAddressId = null)
                    next
                }
            }
        }
    }

    fun showAddForm() = _uiState.update { it.copy(showAddForm = true, formError = null) }
    fun hideAddForm() = _uiState.update {
        it.copy(showAddForm = false, fullName = "", phone = "", street = "", city = "", state = "", pincode = "", formError = null)
    }

    fun onFullNameChange(v: String) = _uiState.update { it.copy(fullName = v) }
    fun onPhoneChange(v: String) = _uiState.update { it.copy(phone = v) }
    fun onStreetChange(v: String) = _uiState.update { it.copy(street = v) }
    fun onCityChange(v: String) = _uiState.update { it.copy(city = v) }
    fun onStateChange(v: String) = _uiState.update { it.copy(state = v) }
    fun onPincodeChange(v: String) = _uiState.update { it.copy(pincode = v) }

    fun selectAddress(addressId: String) = _uiState.update { it.copy(selectedAddressId = addressId) }

    fun saveAddress(): Boolean {
        val s = _uiState.value
        if (s.fullName.isBlank() || s.phone.isBlank() || s.street.isBlank() ||
            s.city.isBlank() || s.state.isBlank() || s.pincode.isBlank()) {
            _uiState.update { it.copy(formError = "Please fill all fields") }
            return false
        }
        if (s.phone.length != 10 || !s.phone.all { it.isDigit() }) {
            _uiState.update { it.copy(formError = "Enter a valid 10-digit phone number") }
            return false
        }
        if (s.pincode.length != 6 || !s.pincode.all { it.isDigit() }) {
            _uiState.update { it.copy(formError = "Enter a valid 6-digit pincode") }
            return false
        }
        val newAddress = Address(
            fullName = s.fullName.trim(),
            phone = s.phone.trim(),
            street = s.street.trim(),
            city = s.city.trim(),
            state = s.state.trim(),
            pincode = s.pincode.trim(),
            isDefault = addressRepository.addresses.value.isEmpty()
        )
        _uiState.update { it.copy(isSaving = true, formError = null) }
        viewModelScope.launch {
            addressRepository.addAddressAndWait(newAddress)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            showAddForm = false,
                            fullName = "", phone = "", street = "", city = "", state = "", pincode = "",
                            formError = null,
                            isSaving = false
                        )
                    }
                    // selectedAddressId will be updated when addresses flow emits after refresh
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            formError = e.message ?: "Failed to save address"
                        )
                    }
                }
        }
        return true
    }

    fun removeAddress(addressId: String) {
        addressRepository.removeAddress(addressId)
        _uiState.update { current ->
            val newSelected = if (current.selectedAddressId == addressId) null else current.selectedAddressId
            current.copy(selectedAddressId = newSelected)
        }
    }
}
