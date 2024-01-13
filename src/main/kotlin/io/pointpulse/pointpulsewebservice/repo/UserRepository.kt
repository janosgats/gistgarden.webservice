package io.pointpulse.pointpulsewebservice.repo

import io.pointpulse.pointpulsewebservice.entity.User
import io.pointpulse.pointpulsewebservice.util.problemrelay.exception.ProducedProblemRelayException
import io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.markers.UserProblemMarker
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

interface UserRepository : JpaRepository<User, Long>

fun UserRepository.findByIdOrThrow(userId: Long, referenceToTheUser: String = "User"): User {
    return this.findByIdOrNull(userId)
        ?: throw ProducedProblemRelayException(UserProblemMarker.USER_NOT_FOUND, message = "$referenceToTheUser not found by ID: $userId")
}

fun UserRepository.findInitiatorByIdOrThrow(userId: Long, referenceToTheUser: String = "Initiator user"): User {
    return this.findByIdOrThrow(userId, referenceToTheUser)
}