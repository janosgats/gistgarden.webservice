package com.gistgarden.gistgardenwebservice.service

import com.gistgarden.gistgardenwebservice.entity.Group
import com.gistgarden.gistgardenwebservice.entity.User
import com.gistgarden.gistgardenwebservice.repo.GroupMembershipRepository
import org.springframework.stereotype.Service

@Service
class GroupMembershipService(
    private val groupMembershipRepository: GroupMembershipRepository,
) {

    fun isUserMemberOfGroup(user: User, group: Group): Boolean {
        return groupMembershipRepository.findByUserAndGroup(user, group) != null
    }
}