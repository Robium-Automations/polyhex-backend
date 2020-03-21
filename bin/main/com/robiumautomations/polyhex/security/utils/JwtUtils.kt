package com.robiumautomations.polyhex.security.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.robiumautomations.polyhex.enums.UserRole
import com.robiumautomations.polyhex.security.AuthenticationUser
import com.robiumautomations.polyhex.security.utils.SecurityConstants.SALT
import com.robiumautomations.polyhex.security.utils.SecurityConstants.SECRET
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.codec.Hex
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

@Component
class JwtUtils {

  fun parseToken(token: String) = Companion.parseToken(token)

  fun generateToken(user: AuthenticationUser): String {
    return JWT.create()
        .withSubject(user.userId)
        .withClaim("username", user.username)
        .withClaim("role", user.role.toString())
        .withExpiresAt(Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(SECRET.toByteArray()))
  }

  companion object {

    fun hashPassword(password: String): String {
      try {
        val messageDigest = MessageDigest.getInstance("MD5")
        val result = messageDigest.digest((password + SALT).toByteArray())
        return String(Hex.encode(result))
      } catch (e: NoSuchAlgorithmException) {
        throw RuntimeException(e)
      }
    }

    fun parseToken(token: String): AuthenticationUser? {
      return try {
        val jwt = JWT.require(Algorithm.HMAC512(SECRET.toByteArray()))
            .build()
            .verify(token.removePrefix(SecurityConstants.TOKEN_PREFIX))

        val userRole = jwt.getClaim("role").asString()
        AuthenticationUser(
            userId = jwt.subject,
            username = jwt.getClaim("username").asString(),
            authorities = listOf(SimpleGrantedAuthority(userRole)),
            role = UserRole.valueOf(userRole)
        )
      } catch (e: Exception) {
        e.printStackTrace()
        null
      }
    }
  }
}