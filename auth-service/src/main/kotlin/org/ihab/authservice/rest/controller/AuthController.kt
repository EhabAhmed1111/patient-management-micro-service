package org.ihab.authservice.rest.controller

import jakarta.validation.Valid
import org.ihab.authservice.data.dto.LoginRequestDTO
import org.ihab.authservice.data.dto.LoginResponseDTO
import org.ihab.authservice.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class AuthController (
    private val authService: AuthService,
){

    @PostMapping("/login")
    fun login(@RequestBody @Valid loginRequestDTO: LoginRequestDTO): ResponseEntity<LoginResponseDTO> {
        val token: String? = authService.authenticate(loginRequestDTO)

        if (token == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        return ResponseEntity.ok(LoginResponseDTO(token))
    }

}