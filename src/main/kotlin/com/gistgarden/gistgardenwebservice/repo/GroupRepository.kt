package com.gistgarden.gistgardenwebservice.repo

import com.gistgarden.gistgardenwebservice.entity.Group
import com.gistgarden.gistgardenwebservice.util.problemrelay.exception.ProducedProblemRelayException
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.markers.GroupProblemMarker
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

interface GroupRepository : JpaRepository<Group, Long> {

    @Transactional
    @Modifying
    @Query(
        "update Group g " +
                "set g.lastActivityAt = :timeToUpdateTo " +
                "where g.id = :groupId AND g.lastActivityAt < :timeToUpdateTo"
    )
    fun updateLastActivityAt_ifNewer(groupId: Long, timeToUpdateTo: Instant)
}


fun GroupRepository.findByIdOrThrow(groupId: Long, message: String = "Group not found by ID: $groupId"): Group {
    return this.findByIdOrNull(groupId)
        ?: throw ProducedProblemRelayException(GroupProblemMarker.GROUP_NOT_FOUND, message = message)
}