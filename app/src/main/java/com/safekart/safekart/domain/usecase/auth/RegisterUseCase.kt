package com.safekart.safekart.domain.usecase.auth

import com.safekart.safekart.data.model.User
import com.safekart.safekart.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case for user registration
 * Contains business logic for user registration
 */
class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        username: String? = null,  // Optional - backend will use email prefix if not provided
        phone: String? = null
    ): Result<User> {
        // Register user with backend API
        // The backend already creates the user record, so we just need to call the API
        return authRepository.createUserWithEmailAndPassword(
            email = email,
            password = password,
            fullName = username?.takeIf { it.isNotBlank() },  // Only pass if not empty
            phone = phone
        )
    }
}

