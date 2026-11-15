package org.ihab.authservice.service

import org.ihab.authservice.data.model.User
import org.ihab.authservice.data.dto.LoginRequestDTO
import org.ihab.authservice.data.repository.UserRepo
import org.ihab.authservice.util.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class AuthService(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
) {

    fun authenticate(loginRequestDTO: LoginRequestDTO): String? {
        val token: String? =
            // first we find the user
            userService.findByEmail(loginRequestDTO.email)
                // then we see if the password is the same as in db
                ?.takeIf { passwordEncoder.matches(loginRequestDTO.password, it.password)}
                // if it is the same we generate our token from the email and role
                ?.let {
            jwtUtil.generateToken(it.email, it.role)
        }

        return token
    }

}