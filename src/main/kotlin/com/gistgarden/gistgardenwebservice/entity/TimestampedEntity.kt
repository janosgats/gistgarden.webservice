package com.gistgarden.gistgardenwebservice.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import java.time.Instant

@MappedSuperclass
abstract class TimestampedEntity {
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