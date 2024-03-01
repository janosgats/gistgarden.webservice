package com.gistgarden.gistgardenwebservice.api.controller.userInitiated

import com.gistgarden.gistgardenwebservice.service.uresinitiated.UserInitiatedGroupManagementService
import com.gistgarden.gistgardenwebservice.util.DtoValidator
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/userInitiated/groupManagement")
class UserInitiatedGroupManagementController(
    private val userInitiatedGroupManagementService: UserInitiatedGroupManagementService,
) {

    @PostMapping("/createGroup")
    fun createGroup(@RequestBody request: CreateGroupRequest): CreateGroupResponse {
        DtoValidator.assert(request)

        val newGroup = userInitiatedGroupManagementService.createGroup(initiatorUserId = request.initiatorUserId!!, groupName = request.groupName!!)

        return CreateGroupResponse(
            groupId = newGroup.id!!,
            groupName = newGroup.name!!,
        )
    }

    @PostMapping("/setGroupName")
    fun createGroup(@RequestBody request: SetGroupNameRequest) {
        DtoValidator.assert(request)

        userInitiatedGroupManagementService.setGroupName(request)
    }

    @PostMapping("/addMemberToGroup")
    fun addMemberToGroup(@RequestBody request: AddMemberToGroupRequest) {
        DtoValidator.assert(request)

        userInitiatedGroupManagementService.addMemberToGroup(request)
    }

    @PostMapping("/removeMemberFromGroup")
    fun removeMemberFromGroup(@RequestBody request: RemoveMemberFromGroupRequest) {
        DtoValidator.assert(request)

        userInitiatedGroupManagementService.removeMemberFromGroup(request)
    }

    @PostMapping("/listGroupMembers")
    fun listGroupMembers(@RequestBody request: InitiatorUserIdWithGroupIdRequest): List<SimpleGroupMemberResponse> {
        DtoValidator.assert(request)

        return userInitiatedGroupManagementService.listGroupMembers(request).map {
            SimpleGroupMemberResponse(userId = it.user!!.id!!)
        }
    }

    @GetMapping("/listBelongingGroups")
    fun listBelongingGroups(@RequestParam initiatorUserId: Long): List<SimpleGroupResponse> {
        return userInitiatedGroupManagementService.listBelongingGroups(initiatorUserId).map {
            SimpleGroupResponse(
                id = it.id,
                name = it.name,
                lastActivityAt = it.lastActivityAt,
            )
        }
    }

    @PostMapping("/getGroup")
    fun getGroup(@RequestBody request: InitiatorUserIdWithGroupIdRequest): SimpleGroupResponse {
        DtoValidator.assert(request)

        return userInitiatedGroupManagementService.getGroup(request).let {
            SimpleGroupResponse(
                id = it.id!!,
                name = it.name!!,
                lastActivityAt = it.lastActivityAt!!,
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

class RemoveMemberFromGroupRequest(
    @field:NotNull
    @field:Min(1)
    val userIdToRemove: Long? = null,
) : InitiatorUserIdWithGroupIdRequest()

class SimpleGroupResponse(
    val id: Long,
    val name: String,
    val lastActivityAt: Instant,
)

class SetGroupNameRequest(
    @field:NotNull
    @field:NotEmpty
    val newGroupName: String? = null,
) : InitiatorUserIdWithGroupIdRequest()

class SimpleGroupMemberResponse(
    val userId: Long,
)