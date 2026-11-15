package org.ihab.authservice.data.repository

import org.ihab.authservice.data.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepo: JpaRepository<User, UUID> {

    fun findByEmail(email: String): User?
}