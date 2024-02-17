package com.gistgarden.gistgardenwebservice.api.controller.userInitiated

import com.gistgarden.gistgardenwebservice.service.uresinitiated.UserInitiatedTopicCommentService
import com.gistgarden.gistgardenwebservice.util.DtoValidator
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/userInitiated/topicComment")
class UserInitiatedTopicCommentController(
    private val userInitiatedTopicCommentService: UserInitiatedTopicCommentService,
) {
    @PostMapping("/listCommentsOnTopic")
    fun listCommentsOnTopic(@RequestBody request: InitiatorUserIdWithTopicIdRequest): List<SimpleTopicCommentResponse> {
        DtoValidator.assert(request)

        return userInitiatedTopicCommentService.listCommentsOnTopic(request).map {
            SimpleTopicCommentResponse(
                id = it.id!!,
                topicId = it.topic!!.id!!,
                description = it.description!!,
                creatorUserId = it.creatorUser!!.id!!
            )
        }
    }

    @PostMapping("/createCommentOnTopic")
    fun createCommentOnTopic(@RequestBody request: CreateCommentOnTopicRequest) {
        DtoValidator.assert(request)

        userInitiatedTopicCommentService.createCommentOnTopic(request)
    }

    @PostMapping("/setDescription")
    fun setDescription(@RequestBody request: SetTopicCommentDescriptionRequest) {
        DtoValidator.assert(request)

        userInitiatedTopicCommentService.setCommentDescription(request)
    }

    @DeleteMapping("/deleteComment")
    fun deleteComment(@RequestBody request: InitiatorUserIdWithTopicCommentIdRequest) {
        DtoValidator.assert(request)

        userInitiatedTopicCommentService.deleteComment(request)
    }
}

class SimpleTopicCommentResponse(
    val id: Long,
    val topicId: Long,
    val description: String,
    val creatorUserId: Long,
)


class CreateCommentOnTopicRequest(
    @field:NotNull
    @field:NotBlank
    val commentDescription: String? = null,
) : InitiatorUserIdWithTopicIdRequest()


class SetTopicCommentDescriptionRequest(
    @field:NotNull
    @field:NotBlank
    val newDescription: String? = null
) : InitiatorUserIdWithTopicCommentIdRequest()

open class InitiatorUserIdWithTopicCommentIdRequest(
    @field:NotNull
    @field:Min(1)
    val initiatorUserId: Long? = null,
    @field:NotNull
    @field:Min(1)
    val topicCommentId: Long? = null,
)