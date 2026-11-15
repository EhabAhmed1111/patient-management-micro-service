package org.ihab.authservice.data.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequestDTO(
    @field:NotBlank(message = "The email address is required")
    @field:Email(message = "The email should be valid email address")
    var email: String,

    @field:NotBlank(message = "The password is required")
    @field:Size(min = 8, message = "The password should be 8 characters")
    var password: String
)
