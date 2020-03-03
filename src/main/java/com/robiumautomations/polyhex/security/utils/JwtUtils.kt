package com.robiumautomations.polyhex.security.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.robiumautomations.polyhex.security.AuthenticationUser
import com.robiumautomations.polyhex.security.utils.SecurityConstants.SALT
import com.robiumautomations.polyhex.security.utils.SecurityConstants.SECRET
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.codec.Hex
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

@Component
class JwtUtils {

  fun parseToken(token: String): AuthenticationUser? {
    return try {
      val body = Jwts.parser()
          .setSigningKey(SECRET)
          .parseClaimsJws(token)
          .body

      val userRole = body["role"] as String
      AuthenticationUser(
          userId = body["userId"] as String,
          username = body.subject,
          authorities = listOf(SimpleGrantedAuthority(userRole)),
          role = userRole
      )
    } catch (e: JwtException) {
      null
    } catch (e: ClassCastException) {
      null
    }
  }

  fun generateToken(user: AuthenticationUser): String {
    return JWT.create()
        .withSubject(user.username)
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
  }
}