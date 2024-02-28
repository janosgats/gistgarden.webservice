package com.gistgarden.gistgardenwebservice.api.controller

import com.gistgarden.gistgardenwebservice.service.AuthenticationResult
import com.gistgarden.gistgardenwebservice.service.UserAuthService
import com.gistgarden.gistgardenwebservice.util.DtoValidator
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.session.FindByIndexNameSessionRepository
import org.springframework.session.Session
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.time.Instant

@RestController
@RequestMapping("/api/userAuth")
class UserAuthController(
    private val kotlinIncompatibleSessionRepository: FindByIndexNameSessionRepository<*>,
    private val userAuthService: UserAuthService,
) {
    /**
     * This getter is here to make the typing of FindByIndexNameSessionRepository compatible with Kotlin
     * while still letting spring Dependency Injection find the right bean by the <*> typing
     */
    private val sessionRepository: FindByIndexNameSessionRepository<Session>
        get() = kotlinIncompatibleSessionRepository as FindByIndexNameSessionRepository<Session>


    @PostMapping("/createSessionByPasswordLogin")
    fun loginByPassword(@RequestBody request: CreateSessionByPasswordLoginRequest): CreateSessionResponse {
        DtoValidator.assert(request)

        val authenticationResult = userAuthService.authenticateUserByPasswordLogin(request.email!!, request.password!!)

        if (authenticationResult.result != AuthenticationResult.SUCCESS) {
            return CreateSessionResponse(areCredentialsValid = false)
        }

        val session = sessionRepository.createSession()
        session.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, authenticationResult.authenticatedUser!!.id!!.toString())

        session.lastAccessedTime = Instant.now()
        if (request.keepMeLoggedIn!!) {
            session.maxInactiveInterval = Duration.ofDays(21)
        }

        sessionRepository.save(session)

        return CreateSessionResponse(
            areCredentialsValid = true,
            userId = authenticationResult.authenticatedUser.id!!,
            sessionId = session.id,
            sessionExpiry = session.lastAccessedTime + session.maxInactiveInterval,
        )
    }

    @PostMapping("/resolveLoginStatusFromSessionId")
    fun resolveLoginStatusFromSessionId(@RequestBody request: ResolveSessionIdRequest): ResolveLoginStatusFromSessionIdResponse {
        DtoValidator.assert(request)

        val session = sessionRepository.findById(request.sessionId) ?: return ResolveLoginStatusFromSessionIdResponse(isSessionValid = false)

        val userIdFromSession: Long? = session.getAttribute<String?>(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME)?.toLong()

        session.lastAccessedTime = Instant.now()
        sessionRepository.save(session)

        return ResolveLoginStatusFromSessionIdResponse(
            isSessionValid = true,
            userId = userIdFromSession,
            sessionExpiry = session.lastAccessedTime + session.maxInactiveInterval,
        )
    }
}

class CreateSessionByPasswordLoginRequest(
    @field:NotNull
    @field:NotBlank
    val email: String? = null,
    @field:NotNull
    @field:NotBlank
    val password: String? = null,
    @field:NotNull
    val keepMeLoggedIn: Boolean? = null,
)


class CreateSessionResponse(
    val areCredentialsValid: Boolean,
    val userId: Long? = null,
    val sessionId: String? = null,
    val sessionExpiry: Instant? = null,
)


class ResolveLoginStatusFromSessionIdResponse(
    val isSessionValid: Boolean,
    val userId: Long? = null,
    val sessionExpiry: Instant? = null,
)


class ResolveSessionIdRequest(
    @field:NotNull
    @field:NotBlank
    val sessionId: String? = null
)