package com.safekart.safekart.domain.usecase.auth

import com.safekart.safekart.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case for sending password reset email
 */
class SendPasswordResetUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        return authRepository.sendPasswordResetEmail(email)
    }
}

