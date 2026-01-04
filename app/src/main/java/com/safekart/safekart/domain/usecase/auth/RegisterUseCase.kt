package com.safekart.safekart.domain.usecase.auth

import com.google.firebase.auth.FirebaseUser
import com.safekart.safekart.domain.repository.AuthRepository
import com.safekart.safekart.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Use case for user registration
 * Contains business logic for user registration and profile creation
 */
class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        username: String
    ): Result<FirebaseUser> {
        return authRepository.createUserWithEmailAndPassword(email, password)
            .onSuccess { user ->
                // Create user document in Firestore
                userRepository.createUser(user.uid, email, username)
                    .onFailure { throwable ->
                        throw Exception("Account created but failed to save user data: ${throwable.message}")
                    }
            }
    }
}

