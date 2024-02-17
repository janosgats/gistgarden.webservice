package com.gistgarden.gistgardenwebservice.entity

import com.gistgarden.gistgardenwebservice.util.problemrelay.exception.ProducedProblemRelayException
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.markers.TopicCommentProblemMarker
import jakarta.persistence.*

@Entity
@Table(name = "topic_comment")
class TopicComment(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    var topic: Topic? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_user_id", nullable = false)
    var creatorUser: User? = null,

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    var description: String? = null,
) : TimestampedEntity()


fun TopicComment?.throwIfNotFound(): TopicComment = this ?: throw ProducedProblemRelayException(TopicCommentProblemMarker.TOPIC_COMMENT_NOT_FOUND)