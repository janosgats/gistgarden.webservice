package com.gistgarden.gistgardenwebservice.service.uresinitiated

import com.gistgarden.gistgardenwebservice.api.controller.userInitiated.*
import com.gistgarden.gistgardenwebservice.entity.Group
import com.gistgarden.gistgardenwebservice.entity.Topic
import com.gistgarden.gistgardenwebservice.entity.User
import com.gistgarden.gistgardenwebservice.entity.throwIfNotFound
import com.gistgarden.gistgardenwebservice.repo.*
import com.gistgarden.gistgardenwebservice.service.GroupService
import org.springframework.stereotype.Service

@Service
class UserInitiatedTopicService(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val topicRepository: TopicRepository,
    private val initiatorUserPermissionHelper: InitiatorUserPermissionHelper,
    private val groupService: GroupService,
) {
    fun createTopicInGroup(request: CreateTopicInGroupRequest) {
        val (initiatorUser, group) = loadInitiatorUserAndGroup(request)

        initiatorUserPermissionHelper.assertIsAllowedTo_createTopicInGroup(initiatorUser, group)

        val newTopic = Topic(
            group = group,
            isDone = false,
            description = request.topicDescription!!,
            creatorUser = initiatorUser,
            isPrivate = request.isPrivate,
            isArchive = false,
        )

        topicRepository.save(newTopic)
        groupService.updateLastActivityInBackground(group)
    }

    fun setTopicIsDoneState(request: SetTopicIsDoneStateRequest) {
        val initiatorUser = loadInitiatorUser(request.initiatorUserId!!)
        val topic = topicRepository.findById_withEagerGroup(request.topicId!!).throwIfNotFound()

        initiatorUserPermissionHelper.assertIsAllowedTo_setIsDoneStateOfTopic(initiatorUser, topic)

        topic.isDone = request.newIsDone!!

        topicRepository.save(topic)
    }

    fun setTopicIsArchiveState(request: SetTopicIsArchiveStateRequest) {
        val initiatorUser = loadInitiatorUser(request.initiatorUserId!!)
        val topic = topicRepository.findById_withEagerGroup(request.topicId!!).throwIfNotFound()

        initiatorUserPermissionHelper.assertIsAllowedTo_setIsArchiveStateOfTopic(initiatorUser, topic)

        topic.isArchive = request.newIsArchive!!

        topicRepository.save(topic)
    }

    fun setTopicIsPrivateState(request: SetTopicIsPrivateStateRequest) {
        val initiatorUser = loadInitiatorUser(request.initiatorUserId!!)
        val topic = topicRepository.findById_withEagerGroup(request.topicId!!).throwIfNotFound()

        initiatorUserPermissionHelper.assertIsAllowedTo_setIsPrivateStateOfTopic(initiatorUser, topic)

        topic.isPrivate = request.newIsPrivate!!

        topicRepository.save(topic)
    }

    fun setTopicDescription(request: SetTopicDescriptionRequest) {
        val initiatorUser = loadInitiatorUser(request.initiatorUserId!!)
        val topic = topicRepository.findById_withEagerGroup(request.topicId!!).throwIfNotFound()

        initiatorUserPermissionHelper.assertIsAllowedTo_setTopicDescription(initiatorUser, topic)

        topic.description = request.newDescription!!

        topicRepository.save(topic)
        groupService.updateLastActivityInBackground(topic.group!!)
    }

    fun deleteTopic(request: InitiatorUserIdWithTopicIdRequest) {
        val initiatorUser = loadInitiatorUser(request.initiatorUserId!!)
        val topic = topicRepository.findById_withEagerGroup(request.topicId!!).throwIfNotFound()

        initiatorUserPermissionHelper.assertIsAllowedTo_deleteTopic(initiatorUser, topic)

        topicRepository.delete(topic)
    }

    fun listTopicsInGroup(request: ListTopicsInGroupRequest): List<Topic> {
        val (initiatorUser, group) = loadInitiatorUserAndGroup(request)

        initiatorUserPermissionHelper.assertIsAllowedTo_listTopicsInGroup(initiatorUser, group)

        val nonArchiveTopics = topicRepository.`find all Topics in Group that are nonPrivate OR ( private AND creatorUser matches viewerUser)`(
            group = group,
            isArchive = false,
            viewerUser = initiatorUser,
        )

        if (!request.includeArchiveTopics!!) {
            return nonArchiveTopics
        }

        val archiveTopics = topicRepository.`find all Topics in Group that are nonPrivate OR ( private AND creatorUser matches viewerUser)`(
            group = group,
            isArchive = true,
            viewerUser = initiatorUser,
        )

        return nonArchiveTopics.plus(archiveTopics)
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