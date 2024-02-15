package com.gistgarden.gistgardenwebservice.api.controller.userInitiated

import com.gistgarden.gistgardenwebservice.service.uresinitiated.UserInitiatedTopicService
import com.gistgarden.gistgardenwebservice.util.DtoValidator
import jakarta.validation.constraints.Min
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
    fun listTopicsInGroup(@RequestBody request: InitiatorUserIdWithGroupIdRequest): List<SimpleTopicResponse> {
        DtoValidator.assert(request)

        return userInitiatedTopicService.listTopicsInGroup(request).map {
            SimpleTopicResponse(
                id = it.id!!,
                isDone = it.isDone!!,
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
    @field:Min(1)
    val initiatorUserId: Long? = null,
    @field:NotNull
    @field:Min(1)
    val topicId: Long? = null,
    @field:NotNull
    val newIsDone: Boolean? = null,
)

class SetTopicIsPrivateStateRequest(
    @field:NotNull
    @field:Min(1)
    val initiatorUserId: Long? = null,
    @field:NotNull
    @field:Min(1)
    val topicId: Long? = null,
    @field:NotNull
    val newIsPrivate: Boolean? = null,
)

class SimpleTopicResponse(
    val id: Long,
    val isDone: Boolean,
    val isPrivate: Boolean,
    val description: String,
    val creatorUserId: Long,
)


class SetTopicDescriptionRequest(
    @field:NotNull
    @field:NotBlank
    val newDescription: String? = null
) : InitiatorUserIdWithTopicIdRequest()

open class InitiatorUserIdWithTopicIdRequest(
    @field:NotNull
    @field:Min(1)
    val initiatorUserId: Long? = null,
    @field:NotNull
    @field:Min(1)
    val topicId: Long? = null,
)