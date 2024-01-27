package com.gistgarden.gistgardenwebservice.api.controller.userInitiated

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

open class InitiatorUserIdWithGroupIdRequest(
    @field:NotNull
    @field:Min(1)
    val initiatorUserId: Long? = null,
    @field:NotNull
    @field:Min(1)
    val groupId: Long? = null,
)