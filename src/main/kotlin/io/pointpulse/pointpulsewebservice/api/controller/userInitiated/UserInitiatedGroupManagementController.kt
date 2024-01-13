package io.pointpulse.pointpulsewebservice.api.controller.userInitiated

import io.pointpulse.pointpulsewebservice.service.uresinitiated.UserInitiatedGroupManagementService
import io.pointpulse.pointpulsewebservice.util.DtoValidator
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.bind.annotation.*

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

    @PostMapping("/addMemberToGroup")
    fun addMemberToGroup(@RequestBody request: AddMemberToGroupRequest) {
        DtoValidator.assert(request)

        userInitiatedGroupManagementService.addMemberToGroup(request)
    }

    @GetMapping("/listBelongingGroups")
    fun listBelongingGroups(@RequestParam initiatorUserId: Long): List<SimpleGroupResponse> {
        return userInitiatedGroupManagementService.listBelongingGroups(initiatorUserId).map {
            SimpleGroupResponse(
                id = it.id,
                name = it.name,
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

class SimpleGroupResponse(
    val id: Long,
    val name: String,
)

