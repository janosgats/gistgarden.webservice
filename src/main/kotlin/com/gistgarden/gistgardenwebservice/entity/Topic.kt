package com.gistgarden.gistgardenwebservice.entity

import jakarta.persistence.*

@Entity
@Table(name = "topic")
class Topic(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    var group: Group? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_user_id", nullable = false)
    var creatorUser: User? = null,

    @Column(name = "is_done", nullable = false)
    var isDone: Boolean? = null,

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    var description: String? = null,
): TimestampedEntity()