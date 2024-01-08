package io.pointpulse.pointpulsewebservice.service

import io.pointpulse.pointpulsewebservice.api.controller.AddMemberToGroupRequest
import io.pointpulse.pointpulsewebservice.api.controller.CreateTopicInGroupRequest
import io.pointpulse.pointpulsewebservice.api.controller.InitiatorUserIdWithGroupIdRequest
import io.pointpulse.pointpulsewebservice.api.controller.SetTopicIsDoneStateRequest
import io.pointpulse.pointpulsewebservice.entity.Group
import io.pointpulse.pointpulsewebservice.entity.GroupMembership
import io.pointpulse.pointpulsewebservice.entity.Topic
import io.pointpulse.pointpulsewebservice.entity.User
import io.pointpulse.pointpulsewebservice.repo.*
import io.pointpulse.pointpulsewebservice.util.problemrelay.exception.ProducedProblemRelayException
import io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.markers.GroupManagementProblemMarker
import io.pointpulse.pointpulsewebservice.util.problemrelaymarkers.markers.TopicProblemMarker
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class UserFacingGroupManagementService(
    private val userRepository: UserRepository,
    private val groupMembershipRepository: GroupMembershipRepository,
    private val groupRepository: GroupRepository,
    private val topicRepository: TopicRepository,
    private val transactionTemplate: TransactionTemplate,
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
        val (_, group) = loadInitiatorUserAndGroupWithAssertedMembership(request)

        val userToAdd = userRepository.findByIdOrThrow(request.userIdToAdd!!)

        if (isUserMemberOfGroup(userToAdd, group)) {
            throw ProducedProblemRelayException(
                GroupManagementProblemMarker.USER_IS_ALREADY_A_MEMBER_OF_THE_GROUP
            )
        }

        val newGroupMembership = GroupMembership(user = userToAdd, group = group)
        groupMembershipRepository.save(newGroupMembership)
    }

    fun createTopicInGroup(request: CreateTopicInGroupRequest) {
        val (_, group) = loadInitiatorUserAndGroupWithAssertedMembership(request)


        val newTopic = Topic(group = group, isDone = false, description = request.topicDescription!!)

        topicRepository.save(newTopic)
    }

    fun setTopicIsDoneState(request: SetTopicIsDoneStateRequest) {
        val initiatorUser = userRepository.findByIdOrThrow(request.initiatorUserId!!, "Initiator user")
        val topic = topicRepository.findById_withEagerGroup(request.topicId!!)
            ?: throw ProducedProblemRelayException(TopicProblemMarker.TOPIC_NOT_FOUND)

        if (!isUserMemberOfGroup(initiatorUser, topic.group!!)) {
            throw ProducedProblemRelayException(
                GroupManagementProblemMarker.USER_IS_NOT_AUTHORIZED_FOR_THIS_ACTION_BECAUSE_IT_IS_NOT_A_MEMBER_OF_THE_GROUP
            )
        }

        topic.isDone = request.isDone!!

        topicRepository.save(topic)
    }

    private fun loadInitiatorUserAndGroupWithAssertedMembership(request: InitiatorUserIdWithGroupIdRequest): Pair<User, Group> {
        val initiatorUser = userRepository.findByIdOrThrow(request.initiatorUserId!!, "Initiator user")
        val group = groupRepository.findByIdOrThrow(request.groupId!!)

        if (!isUserMemberOfGroup(initiatorUser, group)) {
            throw ProducedProblemRelayException(
                GroupManagementProblemMarker.USER_IS_NOT_AUTHORIZED_FOR_THIS_ACTION_BECAUSE_IT_IS_NOT_A_MEMBER_OF_THE_GROUP
            )
        }

        return Pair(initiatorUser, group)
    }

    private fun isUserMemberOfGroup(user: User, group: Group): Boolean {
        return groupMembershipRepository.findByUserAndGroup(user, group) != null
    }
}