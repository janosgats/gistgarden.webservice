package com.gistgarden.gistgardenwebservice.repo

import com.gistgarden.gistgardenwebservice.entity.User
import com.gistgarden.gistgardenwebservice.util.problemrelay.exception.ProducedProblemRelayException
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.markers.UserProblemMarker
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

interface UserRepository : JpaRepository<User, Long> {
    fun findByPrimaryEmail(email: String): User?
}

fun UserRepository.findByIdOrThrow(userId: Long, referenceToTheUser: String = "User"): User {
    return this.findByIdOrNull(userId)
        ?: throw ProducedProblemRelayException(UserProblemMarker.USER_NOT_FOUND, message = "$referenceToTheUser not found by ID: $userId")
}

fun UserRepository.findInitiatorByIdOrThrow(userId: Long, referenceToTheUser: String = "Initiator user"): User {
    return this.findByIdOrThrow(userId, referenceToTheUser)
}