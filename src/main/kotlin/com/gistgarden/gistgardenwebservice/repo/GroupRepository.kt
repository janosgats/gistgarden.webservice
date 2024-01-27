package com.gistgarden.gistgardenwebservice.repo

import com.gistgarden.gistgardenwebservice.entity.Group
import com.gistgarden.gistgardenwebservice.util.problemrelay.exception.ProducedProblemRelayException
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.markers.GroupProblemMarker
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

interface GroupRepository : JpaRepository<Group, Long>


fun GroupRepository.findByIdOrThrow(groupId: Long, message: String = "Group not found by ID: $groupId"): Group {
    return this.findByIdOrNull(groupId)
        ?: throw ProducedProblemRelayException(GroupProblemMarker.GROUP_NOT_FOUND, message = message)
}