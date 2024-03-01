package com.gistgarden.gistgardenwebservice.service

import com.gistgarden.gistgardenwebservice.entity.Group
import com.gistgarden.gistgardenwebservice.repo.GroupRepository
import com.gistgarden.gistgardenwebservice.util.logging.LoggerDelegate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.Instant

@Service
class GroupService(
    private val groupRepository: GroupRepository,
) {
    private val log by LoggerDelegate()

    fun updateLastActivityInBackground(group: Group) = updateLastActivityInBackground(group.id!!)

    fun updateLastActivityInBackground(groupId: Long) {
        val timeOfLastActivity = Instant.now()

        Mono.fromRunnable<Any> {
            groupRepository.updateLastActivityAt_ifNewer(groupId = groupId, timeToUpdateTo = timeOfLastActivity)
        }
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe({}, {
                log.error("Error in updateLastActivityInBackground for group $groupId", it)
            })
    }
}