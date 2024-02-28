package com.gistgarden.gistgardenwebservice.api.controller

import com.gistgarden.gistgardenwebservice.service.RegistrationService
import com.gistgarden.gistgardenwebservice.util.DtoValidator
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/registration")
class RegistrationController(
    private val registrationService: RegistrationService,
) {
    @PostMapping("/submitEmailPasswordRegistrationInquiry")
    fun submitEmailPasswordRegistrationInquiry(@RequestBody request: SubmitEmailPasswordRegistrationInquiryRequest) {
        DtoValidator.assert(request)

        registrationService.submitEmailPasswordRegistrationInquiry(request.email!!.trim(), request.password!!)
    }

    @PostMapping("/verifyEmailForEmailPasswordRegistration")
    fun verifyEmailForEmailPasswordRegistration(@RequestBody request: VerifyEmailForEmailPasswordRegistrationRequest) {
        DtoValidator.assert(request)

        registrationService.verifyEmailForEmailPasswordRegistration(request.inquiryId!!, request.emailVerificationSecret!!)
    }
}

class SubmitEmailPasswordRegistrationInquiryRequest(
    @field:NotNull
    @field:NotBlank
    val email: String? = null,
    @field:NotNull
    @field:NotBlank
    val password: String? = null,
)

class VerifyEmailForEmailPasswordRegistrationRequest(
    @field:NotNull
    @field:Min(1)
    val inquiryId: Long? = null,
    @field:NotNull
    @field:NotBlank
    val emailVerificationSecret: String? = null,
)
