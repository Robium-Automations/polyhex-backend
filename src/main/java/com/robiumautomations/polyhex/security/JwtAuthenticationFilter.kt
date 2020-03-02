package com.robiumautomations.polyhex.security

import com.robiumautomations.polyhex.security.exceptions.JwtTokenMissingException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.ServletException
import java.io.IOException
import javax.servlet.FilterChain

class JwtAuthenticationFilter : AbstractAuthenticationProcessingFilter("/**") {

  override fun requiresAuthentication(request: HttpServletRequest, response: HttpServletResponse?) = true

  @Throws(AuthenticationException::class)
  override fun attemptAuthentication(
      request: HttpServletRequest,
      response: HttpServletResponse
  ): Authentication {

    val header = request.getHeader("Authorization")

    val prefix = "Bearer "
    if (header == null || !header.startsWith(prefix)) {
      throw JwtTokenMissingException("No JWT token found in request headers")
    }
    val authToken = header.substring(prefix.length)
    val authRequest = JwtAuthenticationToken(authToken)
    return authenticationManager.authenticate(authRequest)
  }

  @Throws(IOException::class, ServletException::class)
  override fun successfulAuthentication(
      request: HttpServletRequest,
      response: HttpServletResponse,
      chain: FilterChain?,
      authResult: Authentication
  ) {
    super.successfulAuthentication(request, response, chain, authResult)
    chain!!.doFilter(request, response)
  }
}