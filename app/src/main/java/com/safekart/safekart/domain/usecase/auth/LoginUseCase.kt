package com.safekart.safekart.domain.usecase.auth

import com.google.firebase.auth.FirebaseUser
import com.safekart.safekart.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case for user login
 * Contains business logic for authentication
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<FirebaseUser> {
        return authRepository.signInWithEmailAndPassword(email, password)
    }
}


