package com.gistgarden.gistgardenwebservice.service.uresinitiated

import com.gistgarden.gistgardenwebservice.api.controller.userInitiated.AddMemberToGroupRequest
import com.gistgarden.gistgardenwebservice.api.controller.userInitiated.InitiatorUserIdWithGroupIdRequest
import com.gistgarden.gistgardenwebservice.api.controller.userInitiated.RemoveMemberFromGroupRequest
import com.gistgarden.gistgardenwebservice.api.controller.userInitiated.SetGroupNameRequest
import com.gistgarden.gistgardenwebservice.entity.Group
import com.gistgarden.gistgardenwebservice.entity.GroupMembership
import com.gistgarden.gistgardenwebservice.entity.User
import com.gistgarden.gistgardenwebservice.repo.*
import com.gistgarden.gistgardenwebservice.service.GroupMembershipService
import com.gistgarden.gistgardenwebservice.util.assertWith
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.markers.GroupManagementProblemMarker
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.time.Instant

@Service
class UserInitiatedGroupManagementService(
    private val userRepository: UserRepository,
    private val groupMembershipRepository: GroupMembershipRepository,
    private val groupRepository: GroupRepository,
    private val transactionTemplate: TransactionTemplate,
    private val groupMembershipService: GroupMembershipService,
    private val initiatorUserPermissionHelper: InitiatorUserPermissionHelper,
) {

    fun createGroup(initiatorUserId: Long, groupName: String): Group {
        val initiatorUser = userRepository.findByIdOrThrow(initiatorUserId)
        val newGroup = Group(name = groupName, lastActivityAt = Instant.now())

        val firstGroupMembership = GroupMembership(user = initiatorUser, group = newGroup, topicIdsInDisplayOrder = emptyList())

        transactionTemplate.executeWithoutResult {
            groupRepository.save(newGroup)
            groupMembershipRepository.save(firstGroupMembership)
        }

        return newGroup
    }

    fun setGroupName(request: SetGroupNameRequest) {
        val (initiatorUser, group) = loadInitiatorUserAndGroup(request)

        initiatorUserPermissionHelper.assertIsAllowedTo_setGroupName(initiatorUser, group)

        group.name = request.newGroupName

        groupRepository.save(group)
    }

    fun addMemberToGroup(request: AddMemberToGroupRequest) {
        val (initiatorUser, group) = loadInitiatorUserAndGroup(request)

        initiatorUserPermissionHelper.assertIsAllowedTo_addMemberToGroup(initiatorUser, group)

        val userToAdd = userRepository.findByIdOrThrow(request.userIdToAdd!!, "User to add to group")

        assertWith(GroupManagementProblemMarker.USER_IS_ALREADY_A_MEMBER_OF_THE_GROUP) {
            !groupMembershipService.isUserMemberOfGroup(userToAdd, group)
        }

        val newGroupMembership = GroupMembership(user = userToAdd, group = group, topicIdsInDisplayOrder = emptyList())
        groupMembershipRepository.save(newGroupMembership)
    }

    fun removeMemberFromGroup(request: RemoveMemberFromGroupRequest) {
        val (initiatorUser, group) = loadInitiatorUserAndGroup(request)

        initiatorUserPermissionHelper.assertIsAllowedTo_removeMemberFromGroup(initiatorUser, group)

        val userToRemove = userRepository.findByIdOrThrow(request.userIdToRemove!!, "User to remove form group")

        val membershipToDelete = groupMembershipRepository.findByUserAndGroup(userToRemove, group)

        assertWith(GroupManagementProblemMarker.USER_IS_NOT_A_MEMBER_OF_THE_GROUP) {
            membershipToDelete != null
        }

        groupMembershipRepository.delete(membershipToDelete!!)
    }

    fun listGroupMembers(request: InitiatorUserIdWithGroupIdRequest): List<GroupMembership> {
        val (initiatorUser, group) = loadInitiatorUserAndGroup(request)

        initiatorUserPermissionHelper.assertIsAllowedTo_listGroupMembers(initiatorUser, group)

        return groupMembershipRepository.findAllByGroup(group)
    }

    fun listBelongingGroups(initiatorUserId: Long): List<GroupIdWithNameAndLastActivityAt> {
        val initiatorUser = loadInitiatorUser(initiatorUserId)


        return groupMembershipRepository.findGroupsBelongingToUser_withEagerGroupName(initiatorUser)
    }

    fun getGroup(request: InitiatorUserIdWithGroupIdRequest): Group {
        val (initiatorUser, group) = loadInitiatorUserAndGroup(request)

        initiatorUserPermissionHelper.assertIsAllowedTo_readGroup(initiatorUser, group)

        return group
    }

    private fun loadInitiatorUserAndGroup(request: InitiatorUserIdWithGroupIdRequest): Pair<User, Group> {
        return Pair(
            loadInitiatorUser(request.initiatorUserId!!),
            loadGroup(request.groupId!!),
        )
    }

    private fun loadInitiatorUser(initiatorUserId: Long): User {
        return userRepository.findInitiatorByIdOrThrow(initiatorUserId)
    }

    private fun loadGroup(groupId: Long): Group {
        return groupRepository.findByIdOrThrow(groupId)
    }
}