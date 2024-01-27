package com.gistgarden.gistgardenwebservice.api.controller

import com.gistgarden.gistgardenwebservice.repo.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userRepository: UserRepository,
) {

    @GetMapping("/userInfo")
    fun getUserInfo(@RequestParam("userId") userId: Long): UserInfoResponse {
        val user = userRepository.findByIdOrNull(userId)!!

        return UserInfoResponse(
            userId = user.id!!,
            nickName = user.nickName ?: "user#${user.id}",
        )
    }

}

class UserInfoResponse(
    val userId: Long,
    val nickName: String,
)