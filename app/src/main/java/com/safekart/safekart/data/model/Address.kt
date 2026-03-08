package com.safekart.safekart.data.model

data class Address(
    val id: String = java.util.UUID.randomUUID().toString(),
    val fullName: String = "",
    val phone: String = "",
    val street: String = "",
    val city: String = "",
    val state: String = "",
    val pincode: String = "",
    val isDefault: Boolean = false
)
