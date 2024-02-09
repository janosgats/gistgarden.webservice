package com.gistgarden.gistgardenwebservice.entity

import jakarta.persistence.*

@Entity
@Table(name = "`group`")
class Group(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String? = null
) : TimestampedEntity()