package io.pointpulse.pointpulsewebservice.api.controller.userInitiated

import io.pointpulse.pointpulsewebservice.service.uresinitiated.UserInitiatedTopicService
import io.pointpulse.pointpulsewebservice.util.DtoValidator
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
                description = it.description!!,
            )
        }
    }

    @PostMapping("/setIsDoneState")
    fun setTopicIsDoneState(@RequestBody request: SetTopicIsDoneStateRequest) {
        DtoValidator.assert(request)

        userInitiatedTopicService.setTopicIsDoneState(request)
    }

    @PostMapping("/setDescription")
    fun setDescription(@RequestBody request: SetTopicDescriptionRequest) {
        DtoValidator.assert(request)

        userInitiatedTopicService.setTopicDescription(request)
    }
}

class CreateTopicInGroupRequest(
    @field:NotNull
    @field:NotBlank
    val topicDescription: String? = null,
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

class SimpleTopicResponse(
    val id: Long,
    val isDone: Boolean,
    val description: String,
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