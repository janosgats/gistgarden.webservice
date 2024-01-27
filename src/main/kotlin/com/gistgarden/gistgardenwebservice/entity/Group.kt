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
    var name: String? = null
) {

    @Column(
        name = "created",
        columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP",
        nullable = false,
        insertable = false,
        updatable = false
    )
    var created: Instant? = null

    @Column(
        name = "updated",
        columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP",
        nullable = false,
        insertable = false,
        updatable = false
    )
    var updated: Instant? = null
}