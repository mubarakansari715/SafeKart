package com.safekart.safekart.domain.usecase.user

import com.safekart.safekart.domain.repository.AuthRepository
import com.safekart.safekart.domain.repository.UserRepository
import javax.inject.Inject

data class UserInfo(
    val username: String?,
    val email: String?
)

/**
 * Use case for getting user information
 */
class GetUserInfoUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<UserInfo> {
        return try {
            val userId = authRepository.getCurrentUserId()
                ?: return Result.failure(Exception("User not logged in"))
            
            val usernameResult = userRepository.getUserName(userId)
            val emailResult = userRepository.getUserEmail(userId)
            
            if (usernameResult.isFailure && emailResult.isFailure) {
                return Result.failure(Exception("Failed to fetch user info"))
            }
            
            Result.success(
                UserInfo(
                    username = usernameResult.getOrNull(),
                    email = emailResult.getOrNull() ?: authRepository.currentUser?.email
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

