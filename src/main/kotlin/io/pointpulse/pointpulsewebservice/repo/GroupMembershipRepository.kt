package io.pointpulse.pointpulsewebservice.repo

import io.pointpulse.pointpulsewebservice.entity.Group
import io.pointpulse.pointpulsewebservice.entity.GroupMembership
import io.pointpulse.pointpulsewebservice.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface GroupMembershipRepository : JpaRepository<GroupMembership, Long> {

    fun findByUserAndGroup(user: User, group: Group): GroupMembership?

}