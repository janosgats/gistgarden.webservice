package com.gistgarden.gistgardenwebservice.entity

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "group_membership",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["group_id", "user_id"], name = "group__user__unique")
    ],
    indexes = [
        Index(
            columnList = "user_id",
            name = "user_id"
        )
    ]
)
class GroupMembership(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    var group: Group? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null,

    @Convert(converter = LongListJsonAttributeConverter::class)
    @Column(name = "topic_ids_in_display_order", columnDefinition = "text", nullable = false)
    var topicIdsInDisplayOrder: List<Long>? = null,
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


class LongListJsonAttributeConverter : AttributeConverter<List<Long>?, String?> {
    companion object {
        private val objectMapper = jacksonObjectMapper()
    }

    override fun convertToDatabaseColumn(attribute: List<Long>?): String? {
        return attribute?.let {
            objectMapper.writeValueAsString(it)
        }
    }

    override fun convertToEntityAttribute(dbData: String?): List<Long>? {
        return dbData?.let {
            objectMapper.readValue<List<Long>>(it)
        }
    }
}