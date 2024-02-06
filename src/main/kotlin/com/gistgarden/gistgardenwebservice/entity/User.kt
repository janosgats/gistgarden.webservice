package com.gistgarden.gistgardenwebservice.entity

import jakarta.persistence.*

@Entity
@Table(
    name = "user",
    indexes = [
        Index(
            name = "primary_email",
            columnList = "primary_email",
            unique = true,
        )
    ]
)
class User(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "nick_name", nullable = false)
    var nickName: String? = null,

    @Column(name = "primary_email", nullable = false)
    var primaryEmail: String? = null,

    @Column(name = "password", nullable = false)
    var password: String? = null,
) : TimestampedEntity()