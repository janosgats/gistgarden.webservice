package io.pointpulse.pointpulsewebservice.repo

import io.pointpulse.pointpulsewebservice.entity.Group
import io.pointpulse.pointpulsewebservice.util.problemrelay.exception.ProducedProblemRelayException
import io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.ppws.markers.GroupProblemMarker
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

interface GroupRepository : JpaRepository<Group, Long>


fun GroupRepository.findByIdOrThrow(groupId: Long, message: String = "Group not found by ID: $groupId"): Group {
    return this.findByIdOrNull(groupId)
        ?: throw ProducedProblemRelayException(GroupProblemMarker.GROUP_NOT_FOUND, message = message)
}