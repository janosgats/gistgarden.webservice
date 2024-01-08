package io.pointpulse.pointpulsewebservice.api.controller

import io.pointpulse.pointpulsewebservice.service.UserFacingGroupManagementService
import io.pointpulse.pointpulsewebservice.util.DtoValidator
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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

}

class CreateGroupRequest(
    @NotNull
    @Min(1)
    val initiatorUserId: Long? = null,
    @NotNull
    @NotBlank
    val groupName: String? = null,
)

class CreateGroupResponse(
    val groupId: Long,
    val groupName: String,
)

class AddMemberToGroupRequest(
    @NotNull
    @Min(1)
    val userIdToAdd: Long? = null,
) : InitiatorUserIdWithGroupIdRequest()

class CreateTopicInGroupRequest(
    @NotNull
    @NotBlank
    val topicDescription: String? = null,
) : InitiatorUserIdWithGroupIdRequest()

open class InitiatorUserIdWithGroupIdRequest(
    @NotNull
    @Min(1)
    val initiatorUserId: Long? = null,
    @NotNull
    @Min(1)
    val groupId: Long? = null,
)

class SetTopicIsDoneStateRequest(
    @NotNull
    @Min(1)
    val initiatorUserId: Long? = null,
    @NotNull
    @Min(1)
    val topicId: Long? = null,
    @NotNull
    val isDone: Boolean? = null,
)