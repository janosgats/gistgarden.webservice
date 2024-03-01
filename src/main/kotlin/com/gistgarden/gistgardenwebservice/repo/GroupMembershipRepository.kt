package com.gistgarden.gistgardenwebservice.repo

import com.gistgarden.gistgardenwebservice.entity.Group
import com.gistgarden.gistgardenwebservice.entity.GroupMembership
import com.gistgarden.gistgardenwebservice.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.Instant

interface GroupMembershipRepository : JpaRepository<GroupMembership, Long> {

    fun findByUserAndGroup(user: User, group: Group): GroupMembership?


    @Query(
        "select " +
                "gm.group.id as id, gm.group.name as name, gm.group.lastActivityAt as lastActivityAt " +
                "from GroupMembership gm " +
                "where gm.user = :user " +
                "order by gm.group.lastActivityAt DESC"
    )
    fun findGroupsBelongingToUser_withEagerGroupName(user: User): List<GroupIdWithNameAndLastActivityAt>

    fun findAllByGroup(group: Group): List<GroupMembership>
}

interface GroupIdWithNameAndLastActivityAt {
    val id: Long
    val name: String
    val lastActivityAt: Instant
}