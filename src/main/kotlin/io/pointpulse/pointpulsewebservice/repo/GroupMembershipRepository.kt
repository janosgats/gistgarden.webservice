package io.pointpulse.pointpulsewebservice.repo

import io.pointpulse.pointpulsewebservice.entity.Group
import io.pointpulse.pointpulsewebservice.entity.GroupMembership
import io.pointpulse.pointpulsewebservice.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface GroupMembershipRepository : JpaRepository<GroupMembership, Long> {

    fun findByUserAndGroup(user: User, group: Group): GroupMembership?


    @Query("select gm.group.id as id, gm.group.name as name from GroupMembership gm where gm.user = :user")
    fun findGroupsBelongingToUser_withEagerGroupName(user: User): List<GroupIdWithName>

}

interface GroupIdWithName {
    val id: Long
    val name: String
}