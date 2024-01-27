package com.gistgarden.gistgardenwebservice.repo

import com.gistgarden.gistgardenwebservice.entity.Group
import com.gistgarden.gistgardenwebservice.entity.Topic
import com.gistgarden.gistgardenwebservice.util.problemrelay.exception.ProducedProblemRelayException
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.markers.TopicProblemMarker
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull

interface TopicRepository : JpaRepository<Topic, Long> {

    @EntityGraph(attributePaths = ["group"])
    @Query("select t from Topic t where t.id = :topicId")
    fun findById_withEagerGroup(topicId: Long): Topic?

    fun findAllByGroup(group: Group): List<Topic>
}


fun TopicRepository.findByIdOrThrow(topicId: Long, message: String = "Topic not found by ID: $topicId"): Topic {
    return this.findByIdOrNull(topicId)
        ?: throw ProducedProblemRelayException(TopicProblemMarker.TOPIC_NOT_FOUND, message = message)
}