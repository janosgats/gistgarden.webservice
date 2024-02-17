package com.gistgarden.gistgardenwebservice.repo

import com.gistgarden.gistgardenwebservice.entity.TopicComment
import com.gistgarden.gistgardenwebservice.util.problemrelay.exception.ProducedProblemRelayException
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.markers.TopicCommentProblemMarker
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull

interface TopicCommentRepository : JpaRepository<TopicComment, Long> {

    fun findAllByTopicIdOrderByCreatedAsc(topicId: Long): List<TopicComment>

    @EntityGraph(attributePaths = ["topic", "topic.group"])
    @Query("select tc from TopicComment tc where tc.id = :topicCommentId")
    fun findById_withEagerTopicAndGroup(topicCommentId: Long): TopicComment?
}


fun TopicCommentRepository.findByIdOrThrow(topicCommentId: Long, message: String = "TopicComment not found by ID: $topicCommentId"): TopicComment {
    return this.findByIdOrNull(topicCommentId)
        ?: throw ProducedProblemRelayException(TopicCommentProblemMarker.TOPIC_COMMENT_NOT_FOUND, message = message)
}