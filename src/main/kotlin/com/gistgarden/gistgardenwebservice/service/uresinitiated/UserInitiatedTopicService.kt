package com.gistgarden.gistgardenwebservice.service.uresinitiated

import com.gistgarden.gistgardenwebservice.api.controller.userInitiated.*
import com.gistgarden.gistgardenwebservice.entity.Topic
import com.gistgarden.gistgardenwebservice.repo.TopicRepository
import com.gistgarden.gistgardenwebservice.repo.UserRepository
import com.gistgarden.gistgardenwebservice.repo.findInitiatorByIdOrThrow
import com.gistgarden.gistgardenwebservice.util.assertWith
import com.gistgarden.gistgardenwebservice.util.problemrelay.exception.ProducedProblemRelayException
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.markers.GroupManagementProblemMarker
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.markers.TopicProblemMarker
import org.springframework.stereotype.Service

@Service
class UserInitiatedTopicService(
    private val userRepository: UserRepository,
    private val topicRepository: TopicRepository,
    private val initiatorUserHelper: InitiatorUserHelper
) {
    fun createTopicInGroup(request: CreateTopicInGroupRequest) {
        val (initiatorUser, group) = initiatorUserHelper.loadInitiatorUserAndGroupWithAssertedMembership(request)


        val newTopic = Topic(
            group = group,
            isDone = false,
            description = request.topicDescription!!,
            creatorUser = initiatorUser,
        )

        topicRepository.save(newTopic)
    }

    fun setTopicIsDoneState(request: SetTopicIsDoneStateRequest) {
        val initiatorUser = userRepository.findInitiatorByIdOrThrow(request.initiatorUserId!!)
        val topic = topicRepository.findById_withEagerGroup(request.topicId!!)
            ?: throw ProducedProblemRelayException(TopicProblemMarker.TOPIC_NOT_FOUND)

        assertWith(GroupManagementProblemMarker.USER_IS_NOT_AUTHORIZED_FOR_THIS_ACTION_BECAUSE_IT_IS_NOT_A_MEMBER_OF_THE_GROUP) {
            initiatorUserHelper.isUserMemberOfGroup(initiatorUser, topic.group!!)
        }

        topic.isDone = request.newIsDone!!

        topicRepository.save(topic)
    }

    fun setTopicDescription(request: SetTopicDescriptionRequest) {
        val initiatorUser = userRepository.findInitiatorByIdOrThrow(request.initiatorUserId!!)
        val topic = topicRepository.findById_withEagerGroup(request.topicId!!)
            ?: throw ProducedProblemRelayException(TopicProblemMarker.TOPIC_NOT_FOUND)

        assertWith(GroupManagementProblemMarker.USER_IS_NOT_AUTHORIZED_FOR_THIS_ACTION_BECAUSE_IT_IS_NOT_A_MEMBER_OF_THE_GROUP) {
            initiatorUserHelper.isUserMemberOfGroup(initiatorUser, topic.group!!)
        }

        assertWith(TopicProblemMarker.USER_IS_NOT_AUTHORIZED_FOR_THIS_ACTION_BECAUSE_IT_IS_NOT_THE_CREATOR_OF_THE_TOPIC) {
            initiatorUser.id!! == topic.creatorUser!!.id!!
        }

        topic.description = request.newDescription!!

        topicRepository.save(topic)
    }

    fun deleteTopic(request: InitiatorUserIdWithTopicIdRequest) {
        val initiatorUser = userRepository.findInitiatorByIdOrThrow(request.initiatorUserId!!)
        val topic = topicRepository.findById_withEagerGroup(request.topicId!!)
            ?: throw ProducedProblemRelayException(TopicProblemMarker.TOPIC_NOT_FOUND)

        assertWith(GroupManagementProblemMarker.USER_IS_NOT_AUTHORIZED_FOR_THIS_ACTION_BECAUSE_IT_IS_NOT_A_MEMBER_OF_THE_GROUP) {
            initiatorUserHelper.isUserMemberOfGroup(initiatorUser, topic.group!!)
        }

        topicRepository.delete(topic)
    }

    fun listTopicsInGroup(request: InitiatorUserIdWithGroupIdRequest): List<Topic> {
        val (_, group) = initiatorUserHelper.loadInitiatorUserAndGroupWithAssertedMembership(request)

        return topicRepository.findAllByGroup(group)
    }
}
