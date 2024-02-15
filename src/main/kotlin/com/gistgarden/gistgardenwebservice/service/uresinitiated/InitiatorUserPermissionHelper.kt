package com.gistgarden.gistgardenwebservice.service.uresinitiated

import com.gistgarden.gistgardenwebservice.entity.Group
import com.gistgarden.gistgardenwebservice.entity.Topic
import com.gistgarden.gistgardenwebservice.entity.User
import com.gistgarden.gistgardenwebservice.service.GroupMembershipService
import com.gistgarden.gistgardenwebservice.util.assertWith
import com.gistgarden.gistgardenwebservice.util.problemrelaymarkers.ggws.markers.UserNotAuthorizedForActionProblemMarker
import org.springframework.stereotype.Service

@Service
class InitiatorUserPermissionHelper(
    private val groupMembershipService: GroupMembershipService,
) {

    fun assertIsAllowedTo_readGroup(initiatorUser: User, group: Group) {
        assert_userIsMemberOfGroup(initiatorUser, group)
    }

    fun assertIsAllowedTo_setGroupName(initiatorUser: User, group: Group) {
        assert_userIsMemberOfGroup(initiatorUser, group)
    }

    fun assertIsAllowedTo_addMemberToGroup(initiatorUser: User, group: Group) {
        assert_userIsMemberOfGroup(initiatorUser, group)
    }

    /**
     * @param topic fetches [Topic.group] if not fetched already
     */
    fun assertIsAllowedTo_setIsDoneStateOfTopic(initiatorUser: User, topic: Topic) {
        assert_userIsMemberOfGroup(initiatorUser, topic.group!!)

        if (topic.isPrivate!!) {
            assert_userIsCreatorOfTopic(initiatorUser, topic)
        }
    }

    /**
     * @param topic fetches [Topic.group] if not fetched already
     */
    fun assertIsAllowedTo_setIsPrivateStateOfTopic(initiatorUser: User, topic: Topic) {
        assert_userIsMemberOfGroup(initiatorUser, topic.group!!)

        assert_userIsCreatorOfTopic(initiatorUser, topic)
    }

    fun assertIsAllowedTo_createTopicInGroup(initiatorUser: User, group: Group) {
        assert_userIsMemberOfGroup(initiatorUser, group)
    }

    fun assertIsAllowedTo_listTopicsInGroup(initiatorUser: User, group: Group) {
        assert_userIsMemberOfGroup(initiatorUser, group)
    }

    /**
     * @param topic fetches [Topic.group] if not fetched already
     */
    fun assertIsAllowedTo_setTopicDescription(initiatorUser: User, topic: Topic) {
        assert_userIsMemberOfGroup(initiatorUser, topic.group!!)

        assert_userIsCreatorOfTopic(initiatorUser, topic)
    }

    /**
     * @param topic fetches [Topic.group] if not fetched already
     */
    fun assertIsAllowedTo_deleteTopic(initiatorUser: User, topic: Topic) {
        assert_userIsMemberOfGroup(initiatorUser, topic.group!!)

        if (topic.isPrivate!!) {
            assert_userIsCreatorOfTopic(initiatorUser, topic)
        }
    }

    private fun assert_userIsMemberOfGroup(user: User, group: Group) {
        assertWith(UserNotAuthorizedForActionProblemMarker.HAS_TO_BE_MEMBER_OF_THE_GROUP) {
            groupMembershipService.isUserMemberOfGroup(user, group)
        }
    }

    private fun assert_userIsCreatorOfTopic(user: User, topic: Topic) {
        assertWith(UserNotAuthorizedForActionProblemMarker.HAS_TO_BE_THE_CREATOR_OF_THE_TOPIC) {
            user.id!! == topic.creatorUser!!.id
        }
    }
}