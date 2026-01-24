package com.safekart.safekart.data.model

/**
 * User data model for API responses
 * Matches backend schema: users table + customer_profiles/vendor_profiles
 * 
 * Fields:
 * - id, email, phone, role, created_at, updated_at: from users table
 * - full_name: from customer_profiles.full_name or vendor_profiles.business_name
 */
data class User(
    val id: String,
    val email: String,
    val full_name: String? = null,  // From customer_profiles.full_name or vendor_profiles.business_name
    val phone: String? = null,       // From users.phone or customer_profiles.phone
    val role: String = "customer",
    val created_at: String? = null,
    val updated_at: String? = null   // From users.updated_at
)
