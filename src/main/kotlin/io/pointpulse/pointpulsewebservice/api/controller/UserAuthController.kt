package io.pointpulse.pointpulsewebservice.api.controller

import io.pointpulse.pointpulsewebservice.util.DtoValidator
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private const val MOCK_SESSION_ID_FOR_USER_1 = "mockSessionIdForUser1"

@RestController
@RequestMapping("/api/userAuth")
class UserAuthController {

    @PostMapping("/mockLogin")
    fun mockLogin(): LoginResponse {
        return LoginResponse(
            sessionId = MOCK_SESSION_ID_FOR_USER_1
        )
    }

    @PostMapping("/resolveSessionId")
    fun resolveSessionId(@RequestBody request: ResolveSessionIdRequest): ResolveSessionIdResponse {
        DtoValidator.assert(request)

        if (request.sessionId == MOCK_SESSION_ID_FOR_USER_1) {
            return ResolveSessionIdResponse(
                userId = 1L,
            )
        }

        TODO("Auth is only implemented for testing with user 1")
    }
}

class LoginResponse(
    val sessionId: String,
)

class ResolveSessionIdRequest(
    @field:NotNull
    @field:NotBlank
    val sessionId: String?
)

class ResolveSessionIdResponse(
    val userId: Long,
)