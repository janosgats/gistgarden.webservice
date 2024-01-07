package io.pointpulse.pointpulsewebservice.repo

import io.pointpulse.pointpulsewebservice.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>