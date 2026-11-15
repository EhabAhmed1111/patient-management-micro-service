package org.ihab.authservice.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.ihab.authservice.data.model.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Base64
import java.util.Date

@Component
class JwtUtil(
    @Value("\${jwt.secret}")
    secret: String,
) {
    private val secretKey: Key

    init {
        // here no need to use secret.toByte becuase the method already take string or butearray
        val keyBytes = Base64.getDecoder().decode(secret.toByteArray(Charsets.UTF_8))
        secretKey = Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateToken(email: String, role: String): String {
        return Jwts.builder()
            .subject(email)
            .claim("role", role)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))// 10 hours
            .signWith(secretKey)
            .compact()
    }
}