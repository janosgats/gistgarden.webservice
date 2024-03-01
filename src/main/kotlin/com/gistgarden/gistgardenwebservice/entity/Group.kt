package com.gistgarden.gistgardenwebservice.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "`group`")
class Group(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String? = null,

    @Column(name = "last_activity_at", columnDefinition = "datetime", nullable = false)
    var lastActivityAt: Instant? = null
) : TimestampedEntity()