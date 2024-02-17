package com.gistgarden.gistgardenwebservice.service.uresinitiated

import com.gistgarden.gistgardenwebservice.api.controller.userInitiated.CreateCommentOnTopicRequest
import com.gistgarden.gistgardenwebservice.api.controller.userInitiated.InitiatorUserIdWithTopicCommentIdRequest
import com.gistgarden.gistgardenwebservice.api.controller.userInitiated.InitiatorUserIdWithTopicIdRequest
import com.gistgarden.gistgardenwebservice.api.controller.userInitiated.SetTopicCommentDescriptionRequest
import com.gistgarden.gistgardenwebservice.entity.Topic
import com.gistgarden.gistgardenwebservice.entity.TopicComment
import com.gistgarden.gistgardenwebservice.entity.User
import com.gistgarden.gistgardenwebservice.entity.throwIfNotFound
import com.gistgarden.gistgardenwebservice.repo.TopicCommentRepository
import com.gistgarden.gistgardenwebservice.repo.TopicRepository
import com.gistgarden.gistgardenwebservice.repo.UserRepository
import com.gistgarden.gistgardenwebservice.repo.findInitiatorByIdOrThrow
import org.springframework.stereotype.Service

@Service
class UserInitiatedTopicCommentService(
    private val userRepository: UserRepository,
    private val topicRepository: TopicRepository,
    private val topicCommentRepository: TopicCommentRepository,
    private val initiatorUserPermissionHelper: InitiatorUserPermissionHelper,
) {

    fun listCommentsOnTopic(request: InitiatorUserIdWithTopicIdRequest): List<TopicComment> {
        val (initiatorUser, topic) = loadInitiatorUserAndTopicWithEagerGroup(request)

        initiatorUserPermissionHelper.assertIsAllowedTo_listCommentsOnTopic(initiatorUser, topic)

        return topicCommentRepository.findAllByTopicIdOrderByCreatedAsc(topicId = topic.id!!)
    }

    fun createCommentOnTopic(request: CreateCommentOnTopicRequest) {
        val (initiatorUser, topic) = loadInitiatorUserAndTopicWithEagerGroup(request)

        initiatorUserPermissionHelper.assertIsAllowedTo_createCommentOnTopic(initiatorUser, topic)

        val newComment = TopicComment(
            topic = topic,
            creatorUser = initiatorUser,
            description = request.commentDescription!!,
        )

        topicCommentRepository.save(newComment)
    }

    fun setCommentDescription(request: SetTopicCommentDescriptionRequest) {
        val initiatorUser = loadInitiatorUser(request.initiatorUserId!!)

        val comment = topicCommentRepository.findById_withEagerTopicAndGroup(request.topicCommentId!!).throwIfNotFound()

        initiatorUserPermissionHelper.assertIsAllowedTo_setCommentDescription(initiatorUser, comment)

        comment.description = request.newDescription!!

        topicCommentRepository.save(comment)
    }

    fun deleteComment(request: InitiatorUserIdWithTopicCommentIdRequest) {
        val initiatorUser = loadInitiatorUser(request.initiatorUserId!!)

        val comment = topicCommentRepository.findById_withEagerTopicAndGroup(request.topicCommentId!!).throwIfNotFound()

        initiatorUserPermissionHelper.assertIsAllowedTo_deleteComment(initiatorUser, comment)

        topicCommentRepository.delete(comment)
    }

    private fun loadInitiatorUserAndTopicWithEagerGroup(request: InitiatorUserIdWithTopicIdRequest): Pair<User, Topic> {
        return Pair(
            loadInitiatorUser(request.initiatorUserId!!),
            loadTopicWithEagerGroup(request.topicId!!),
        )
    }

    private fun loadInitiatorUser(initiatorUserId: Long): User {
        return userRepository.findInitiatorByIdOrThrow(initiatorUserId)
    }

    private fun loadTopicWithEagerGroup(topicId: Long): Topic {
        return topicRepository.findById_withEagerGroup(topicId).throwIfNotFound()
    }

}
