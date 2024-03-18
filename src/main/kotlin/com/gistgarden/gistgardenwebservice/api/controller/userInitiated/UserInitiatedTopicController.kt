package com.gistgarden.gistgardenwebservice.api.controller.userInitiated

import com.gistgarden.gistgardenwebservice.service.uresinitiated.UserInitiatedTopicService
import com.gistgarden.gistgardenwebservice.util.DtoValidator
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/userInitiated/topic")
class UserInitiatedTopicController(
    private val userInitiatedTopicService: UserInitiatedTopicService,
) {

    @PostMapping("/createTopicInGroup")
    fun createTopicInGroup(@RequestBody request: CreateTopicInGroupRequest) {
        DtoValidator.assert(request)

        userInitiatedTopicService.createTopicInGroup(request)
    }

    @PostMapping("/listTopicsInGroup")
    fun listTopicsInGroup(@RequestBody request: ListTopicsInGroupRequest): List<SimpleTopicResponse> {
        DtoValidator.assert(request)

        return userInitiatedTopicService.listTopicsInGroup(request).map {
            SimpleTopicResponse(
                id = it.id!!,
                isDone = it.isDone!!,
                isArchive = it.isArchive!!,
                isPrivate = it.isPrivate!!,
                description = it.description!!,
                creatorUserId = it.creatorUser!!.id!!
            )
        }
    }

    @PostMapping("/setIsDoneState")
    fun setTopicIsDoneState(@RequestBody request: SetTopicIsDoneStateRequest) {
        DtoValidator.assert(request)

        userInitiatedTopicService.setTopicIsDoneState(request)
    }

    @PostMapping("/setIsArchiveState")
    fun setTopicIsArchiveState(@RequestBody request: SetTopicIsArchiveStateRequest) {
        DtoValidator.assert(request)

        userInitiatedTopicService.setTopicIsArchiveState(request)
    }

    @PostMapping("/setIsPrivateState")
    fun setTopicIsDoneState(@RequestBody request: SetTopicIsPrivateStateRequest) {
        DtoValidator.assert(request)

        userInitiatedTopicService.setTopicIsPrivateState(request)
    }

    @PostMapping("/setDescription")
    fun setDescription(@RequestBody request: SetTopicDescriptionRequest) {
        DtoValidator.assert(request)

        userInitiatedTopicService.setTopicDescription(request)
    }

    @DeleteMapping("/deleteTopic")
    fun deleteTopic(@RequestBody request: InitiatorUserIdWithTopicIdRequest) {
        DtoValidator.assert(request)

        userInitiatedTopicService.deleteTopic(request)
    }
}

class CreateTopicInGroupRequest(
    @field:NotNull
    @field:NotBlank
    val topicDescription: String? = null,
    @field:NotNull
    val isPrivate: Boolean? = null,
) : InitiatorUserIdWithGroupIdRequest()

class SetTopicIsDoneStateRequest(
    @field:NotNull
    val newIsDone: Boolean? = null,
) : InitiatorUserIdWithTopicIdRequest()

class SetTopicIsArchiveStateRequest(
    @field:NotNull
    val newIsArchive: Boolean? = null,
) : InitiatorUserIdWithTopicIdRequest()

class SetTopicIsPrivateStateRequest(
    @field:NotNull
    val newIsPrivate: Boolean? = null,
) : InitiatorUserIdWithTopicIdRequest()

class SimpleTopicResponse(
    val id: Long,
    val isDone: Boolean,
    val isArchive: Boolean,
    val isPrivate: Boolean,
    val description: String,
    val creatorUserId: Long,
)


class SetTopicDescriptionRequest(
    @field:NotNull
    @field:NotBlank
    val newDescription: String? = null
) : InitiatorUserIdWithTopicIdRequest()

class ListTopicsInGroupRequest(
    @field:NotNull
    val includeArchiveTopics: Boolean? = null,
) : InitiatorUserIdWithGroupIdRequest()