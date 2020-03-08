package com.robiumautomations.polyhex.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm.HMAC512
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.ServletException
import java.io.IOException
import javax.servlet.FilterChain
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import com.fasterxml.jackson.databind.ObjectMapper
import com.robiumautomations.polyhex.models.UserCredentials
import com.robiumautomations.polyhex.security.utils.SecurityConstants.EXPIRATION_TIME
import com.robiumautomations.polyhex.security.utils.SecurityConstants.HEADER_STRING
import com.robiumautomations.polyhex.security.utils.SecurityConstants.SECRET
import com.robiumautomations.polyhex.security.utils.SecurityConstants.TOKEN_PREFIX
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import java.util.*

class JwtAuthenticationFilter(
    private val authManager: AuthenticationManager
) : UsernamePasswordAuthenticationFilter() {

  @Throws(AuthenticationException::class)
  override fun attemptAuthentication(
      req: HttpServletRequest,
      res: HttpServletResponse?
  ): Authentication {
    try {
      val credentials = ObjectMapper()
          .readValue(req.inputStream, UserCredentials::class.java)

      return authManager.authenticate(
          UsernamePasswordAuthenticationToken(
              credentials.username,
              credentials.password,
              listOf<GrantedAuthority>()
          )
      )
    } catch (e: IOException) {
      throw RuntimeException(e)
    }
  }

  @Throws(IOException::class, ServletException::class)
  override fun successfulAuthentication(
      req: HttpServletRequest,
      res: HttpServletResponse,
      chain: FilterChain?,
      auth: Authentication
  ) {
    val token = JWT.create()
        .withSubject((auth.principal as User).username)
        .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .sign(HMAC512(SECRET.toByteArray()))
    res.addHeader(HEADER_STRING, TOKEN_PREFIX + token)
  }
}