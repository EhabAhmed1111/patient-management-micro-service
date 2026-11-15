package org.ihab.authservice.service

import org.ihab.authservice.data.model.User
import org.ihab.authservice.data.repository.UserRepo
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepo: UserRepo
) {
    fun findByEmail(email: String): User? = userRepo.findByEmail(email)
}