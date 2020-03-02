package com.robiumautomations.polyhex.security.utils

import com.robiumautomations.polyhex.models.UserCredentials
import com.robiumautomations.polyhex.security.AuthenticationUser
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.codec.Hex
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

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
          role = userRole,
          token = token
      )
    } catch (e: JwtException) {
      null
    } catch (e: ClassCastException) {
      null
    }
  }

  fun generateToken(user: UserCredentials): String {
    val claims = Jwts.claims().setSubject(user.username)
    claims["userId"] = user.userId
    claims["role"] = user.userRole

    return Jwts.builder()
        .setClaims(claims)
        .signWith(SignatureAlgorithm.HS512, SECRET)
        .compact()
  }

  companion object {

    private val SALT = "kssakmdsfbhsbdh4323423dhs" // TODO(move to application.properties)

    private val SECRET = "abcdef" // TODO(move to application.properties)

    fun hashPassword(password: String): String {
      try {
        val messageDigest = MessageDigest.getInstance("MD5")
        val result = messageDigest.digest((SALT + password + SALT).toByteArray())
        return String(Hex.encode(result))
      } catch (e: NoSuchAlgorithmException) {
        throw RuntimeException(e)
      }
    }
  }
}