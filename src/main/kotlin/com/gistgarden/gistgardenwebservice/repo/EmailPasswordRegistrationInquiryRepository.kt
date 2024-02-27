package com.gistgarden.gistgardenwebservice.repo

import com.gistgarden.gistgardenwebservice.entity.EmailPasswordRegistrationInquiry
import com.gistgarden.gistgardenwebservice.util.problemrelay.exception.ProducedProblemRelayException
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.markers.RegistrationProblemMarker
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

interface EmailPasswordRegistrationInquiryRepository : JpaRepository<EmailPasswordRegistrationInquiry, Long>


fun EmailPasswordRegistrationInquiryRepository.findByIdOrThrow(
    inquiryId: Long,
    message: String = "EmailPasswordRegistrationInquiry not found by ID: $inquiryId"
): EmailPasswordRegistrationInquiry {
    return this.findByIdOrNull(inquiryId)
        ?: throw ProducedProblemRelayException(RegistrationProblemMarker.EMAIL_PASSWORD_REGISTRATION_INQUIRY_NOT_FOUND, message = message)
}