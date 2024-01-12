package io.pointpulse.pointpulsewebservice.api.controller

import io.pointpulse.pointpulsewebservice.service.UserFacingGroupManagementService
import io.pointpulse.pointpulsewebservice.util.DtoValidator
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/userFacingGroupManagement")
class UserFacingGroupManagementController(
    private val userFacingGroupManagementService: UserFacingGroupManagementService,
) {

    @PostMapping("/createGroup")
    fun createGroup(@RequestBody request: CreateGroupRequest): CreateGroupResponse {
        DtoValidator.assert(request)

        val newGroup = userFacingGroupManagementService.createGroup(initiatorUserId = request.initiatorUserId!!, groupName = request.groupName!!)

        return CreateGroupResponse(
            groupId = newGroup.id!!,
            groupName = newGroup.name!!,
        )
    }

    @PostMapping("/addMemberToGroup")
    fun addMemberToGroup(@RequestBody request: AddMemberToGroupRequest) {
        DtoValidator.assert(request)

        userFacingGroupManagementService.addMemberToGroup(request)
    }

    @PostMapping("/createTopicInGroup")
    fun createTopicInGroup(@RequestBody request: CreateTopicInGroupRequest) {
        DtoValidator.assert(request)

        userFacingGroupManagementService.createTopicInGroup(request)
    }

    @PostMapping("/setTopicIsDoneState")
    fun setTopicIsDoneState(@RequestBody request: SetTopicIsDoneStateRequest) {
        DtoValidator.assert(request)

        userFacingGroupManagementService.setTopicIsDoneState(request)
    }

    @GetMapping("/listBelongingGroups")
    fun listBelongingGroups(@RequestParam initiatorUserId: Long): List<SimpleGroupResponse> {
        return userFacingGroupManagementService.listBelongingGroups(initiatorUserId).map {
            SimpleGroupResponse(
                id = it.id,
                name = it.name,
            )
        }
    }

    @PostMapping("/listTopicsInGroup")
    fun listTopicsInGroup(@RequestBody request: InitiatorUserIdWithGroupIdRequest): List<SimpleTopicResponse> {
        DtoValidator.assert(request)

        return userFacingGroupManagementService.listTopicsInGroup(request).map {
            SimpleTopicResponse(
                id = it.id!!,
                isDone = it.isDone!!,
                description = it.description!!,
            )
        }
    }
}

class CreateGroupRequest(
    @field:NotNull
    @field:Min(1)
    val initiatorUserId: Long? = null,
    @field:NotNull
    @field:NotBlank
    val groupName: String? = null,
)

class CreateGroupResponse(
    val groupId: Long,
    val groupName: String,
)

class AddMemberToGroupRequest(
    @field:NotNull
    @field:Min(1)
    val userIdToAdd: Long? = null,
) : InitiatorUserIdWithGroupIdRequest()

class CreateTopicInGroupRequest(
    @field:NotNull
    @field:NotBlank
    val topicDescription: String? = null,
) : InitiatorUserIdWithGroupIdRequest()

open class InitiatorUserIdWithGroupIdRequest(
    @field:NotNull
    @field:Min(1)
    val initiatorUserId: Long? = null,
    @field:NotNull
    @field:Min(1)
    val groupId: Long? = null,
)

class SetTopicIsDoneStateRequest(
    @field:NotNull
    @field:Min(1)
    val initiatorUserId: Long? = null,
    @field:NotNull
    @field:Min(1)
    val topicId: Long? = null,
    @field:NotNull
    val isDone: Boolean? = null,
)

class SimpleGroupResponse(
    val id: Long,
    val name: String,
)

class SimpleTopicResponse(
    val id: Long,
    val isDone: Boolean,
    val description: String,
)