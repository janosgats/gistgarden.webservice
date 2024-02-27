package com.gistgarden.gistgardenwebservice.service

import com.gistgarden.gistgardenwebservice.entity.User
import com.gistgarden.gistgardenwebservice.repo.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserAuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun authenticateUserByPasswordLogin(email: String, enteredPassword: String): AuthenticationResultDto {
        val user = userRepository.findByPrimaryEmail(email) ?: return AuthenticationResultDto(AuthenticationResult.USER_NOT_FOUND)

        val doesPasswordMatch = passwordEncoder.matches(enteredPassword, user.password!!)

        if (doesPasswordMatch) {
            return AuthenticationResultDto(AuthenticationResult.SUCCESS, user)
        }
        return AuthenticationResultDto(AuthenticationResult.PASSWORD_MISMATCH)
    }
}

class AuthenticationResultDto(
    val result: AuthenticationResult,
    val authenticatedUser: User? = null
)

enum class AuthenticationResult(
    val id: Int,
) {
    SUCCESS(1),
    USER_NOT_FOUND(2),
    PASSWORD_MISMATCH(3),
}