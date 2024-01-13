package io.pointpulse.pointpulsewebservice.service.uresinitiated

import io.pointpulse.pointpulsewebservice.api.controller.userInitiated.InitiatorUserIdWithGroupIdRequest
import io.pointpulse.pointpulsewebservice.entity.Group
import io.pointpulse.pointpulsewebservice.entity.User
import io.pointpulse.pointpulsewebservice.repo.*
import io.pointpulse.pointpulsewebservice.util.problemrelay.exception.ProducedProblemRelayException
import io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.markers.GroupManagementProblemMarker
import org.springframework.stereotype.Service

@Service
class InitiatorUserHelper(
    private val userRepository: UserRepository,
    private val groupMembershipRepository: GroupMembershipRepository,
    private val groupRepository: GroupRepository,
) {
    fun loadInitiatorUserAndGroupWithAssertedMembership(request: InitiatorUserIdWithGroupIdRequest): Pair<User, Group> {
        val initiatorUser = userRepository.findInitiatorByIdOrThrow(request.initiatorUserId!!)
        val group = groupRepository.findByIdOrThrow(request.groupId!!)

        if (!isUserMemberOfGroup(initiatorUser, group)) {
            throw ProducedProblemRelayException(
                GroupManagementProblemMarker.USER_IS_NOT_AUTHORIZED_FOR_THIS_ACTION_BECAUSE_IT_IS_NOT_A_MEMBER_OF_THE_GROUP
            )
        }

        return Pair(initiatorUser, group)
    }

    fun isUserMemberOfGroup(user: User, group: Group): Boolean {
        return groupMembershipRepository.findByUserAndGroup(user, group) != null
    }
}