package com.gistgarden.gistgardenwebservice.api.controller

import com.gistgarden.gistgardenwebservice.util.DtoValidator
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private const val MOCK_JWT_FOR_USER_1 = "mockJwtForUser1"

@RestController
@RequestMapping("/api/userAuth")
class UserAuthController {

    @PostMapping("/mockLogin")
    fun mockLogin(): LoginResponse {
        return LoginResponse(
            userId = 1,
            jwt = MOCK_JWT_FOR_USER_1,
        )
    }

    @PostMapping("/resolveJwt")
    fun resolveJwt(@RequestBody request: ResolveJwtRequest): ResolveJwtResponse {
        DtoValidator.assert(request)

        if (request.jwt == MOCK_JWT_FOR_USER_1) {
            return ResolveJwtResponse(
                userId = 1L,
            )
        }

        TODO("Auth is only implemented for testing with user 1")
    }
}

class LoginResponse(
    val userId: Long,
    val jwt: String,
)

class ResolveJwtRequest(
    @field:NotNull
    @field:NotBlank
    val jwt: String?
)

class ResolveJwtResponse(
    val userId: Long,
)