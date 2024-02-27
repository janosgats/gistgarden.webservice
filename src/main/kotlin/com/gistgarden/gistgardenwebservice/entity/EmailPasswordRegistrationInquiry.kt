package com.gistgarden.gistgardenwebservice.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "email_password_registration_inquiry")
class EmailPasswordRegistrationInquiry(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "email", nullable = false)
    var email: String? = null,

    /**
     * hashed
     */
    @Column(name = "password", nullable = false)
    var password: String? = null,

    @Column(name = "email_verification_secret", nullable = false)
    var emailVerificationSecret: String? = null,

    @Column(name = "expiry", nullable = false)
    var expiry: Instant? = null,
) : TimestampedEntity()
