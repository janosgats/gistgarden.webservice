package com.gistgarden.gistgardenwebservice.service

import com.gistgarden.gistgardenwebservice.entity.EmailPasswordRegistrationInquiry
import com.gistgarden.gistgardenwebservice.repo.EmailPasswordRegistrationInquiryRepository
import com.gistgarden.gistgardenwebservice.repo.UserRepository
import com.gistgarden.gistgardenwebservice.service.emailsending.RegistrationEmailService
import com.gistgarden.gistgardenwebservice.util.problemrelay.exception.ProducedProblemRelayException
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.markers.RegistrationProblemMarker
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
class RegistrationService(
    private val userRepository: UserRepository,
    private val emailPasswordRegistrationInquiryRepository: EmailPasswordRegistrationInquiryRepository,
    private val passwordEncoder: PasswordEncoder,
    private val registrationEmailService: RegistrationEmailService,
) {

    fun submitEmailPasswordRegistrationInquiry(email: String, password: String) {
        if (userRepository.findByPrimaryEmail(email) != null) {
            throw ProducedProblemRelayException(RegistrationProblemMarker.EMAIL_IS_ALREADY_TAKEN)
        }

        val newInquiry = EmailPasswordRegistrationInquiry(
            email = email,
            password = passwordEncoder.encode(password),
            emailVerificationSecret = EmailVerificationSecretGenerator.generate(),
            expiry = Instant.now().plus(Duration.ofDays(5)),
        )

        emailPasswordRegistrationInquiryRepository.save(newInquiry)

        registrationEmailService.sendEmailPasswordRegistrationVerificationEmail(
            emailAddress = email,
            inquiryId = newInquiry.id!!,
            inquirySecret = newInquiry.emailVerificationSecret!!
        )
    }
}

private object EmailVerificationSecretGenerator {
    private val base64Encoder = Base64.getEncoder()
    private val secureRandom = SecureRandom()

    fun generate(): String {
        val randomBytes = ByteArray(32)
        secureRandom.nextBytes(randomBytes)
        return String(base64Encoder.encode(randomBytes))
    }
}