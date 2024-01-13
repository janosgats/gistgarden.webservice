package io.pointpulse.pointpulsewebservice.service.uresinitiated

import io.pointpulse.pointpulsewebservice.api.controller.userInitiated.AddMemberToGroupRequest
import io.pointpulse.pointpulsewebservice.api.controller.userInitiated.InitiatorUserIdWithGroupIdRequest
import io.pointpulse.pointpulsewebservice.entity.Group
import io.pointpulse.pointpulsewebservice.entity.GroupMembership
import io.pointpulse.pointpulsewebservice.repo.*
import io.pointpulse.pointpulsewebservice.util.problemrelay.exception.ProducedProblemRelayException
import io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.markers.GroupManagementProblemMarker
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class UserInitiatedGroupManagementService(
    private val userRepository: UserRepository,
    private val groupMembershipRepository: GroupMembershipRepository,
    private val groupRepository: GroupRepository,
    private val transactionTemplate: TransactionTemplate,
    private val initiatorUserHelper: InitiatorUserHelper,
) {

    fun createGroup(initiatorUserId: Long, groupName: String): Group {
        val initiatorUser = userRepository.findByIdOrThrow(initiatorUserId)
        val newGroup = Group(name = groupName)

        val firstGroupMembership = GroupMembership(user = initiatorUser, group = newGroup)

        transactionTemplate.executeWithoutResult {
            groupRepository.save(newGroup)
            groupMembershipRepository.save(firstGroupMembership)
        }

        return newGroup
    }

    fun addMemberToGroup(request: AddMemberToGroupRequest) {
        val (_, group) = initiatorUserHelper.loadInitiatorUserAndGroupWithAssertedMembership(request)

        val userToAdd = userRepository.findByIdOrThrow(request.userIdToAdd!!, "User to add to group")

        if (initiatorUserHelper.isUserMemberOfGroup(userToAdd, group)) {
            throw ProducedProblemRelayException(
                GroupManagementProblemMarker.USER_IS_ALREADY_A_MEMBER_OF_THE_GROUP
            )
        }

        val newGroupMembership = GroupMembership(user = userToAdd, group = group)
        groupMembershipRepository.save(newGroupMembership)
    }

    fun listBelongingGroups(initiatorUserId: Long): List<GroupIdWithName> {
        val initiatorUser = userRepository.findInitiatorByIdOrThrow(initiatorUserId)


        return groupMembershipRepository.findGroupsBelongingToUser_withEagerGroupName(initiatorUser)
    }

    fun getGroup(request: InitiatorUserIdWithGroupIdRequest): Group {
        val (_, group) = initiatorUserHelper.loadInitiatorUserAndGroupWithAssertedMembership(request)

        return group
    }
}