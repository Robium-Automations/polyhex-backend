package com.robiumautomations.polyhex.security

import io.jsonwebtoken.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.slf4j.LoggerFactory
import javax.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import javax.servlet.ServletException
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.security.SignatureException

class JwtAuthorizationFilter(authenticationManager: AuthenticationManager) : BasicAuthenticationFilter(
    authenticationManager) {

  @Throws(IOException::class, ServletException::class)
  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse,
      filterChain: FilterChain) {
    val authentication = getAuthentication(request)
    if (authentication == null) {
      filterChain.doFilter(request, response)
      return
    }

    SecurityContextHolder.getContext().authentication = authentication
    filterChain.doFilter(request, response)
  }

  private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
    val token = request.getHeader("Authorization")
    if (token.isNotEmpty() && token.startsWith("Bearer ")) {
      try {
        val body = Jwts.parser()
            .setSigningKey("abcdef")
            .parseClaimsJws(token)
            .body

        val userRole = body["role"] as String
        val authenticationUser = AuthenticationUser(
            userId = body["userId"] as String,
            username = body.subject,
            authorities = listOf(SimpleGrantedAuthority(userRole)),
            role = userRole,
            token = token
        )

        if (authenticationUser.username.isNotEmpty()) {
          return UsernamePasswordAuthenticationToken(authenticationUser.username, null, authenticationUser.authorities)
        }
      } catch (exception: ExpiredJwtException) {
        log.warn("Request to parse expired JWT : {} failed : {}", token, exception.message)
      } catch (exception: UnsupportedJwtException) {
        log.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.message)
      } catch (exception: MalformedJwtException) {
        log.warn("Request to parse invalid JWT : {} failed : {}", token, exception.message)
      } catch (exception: SignatureException) {
        log.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.message)
      } catch (exception: IllegalArgumentException) {
        log.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.message)
      }
    }

    return null
  }

  companion object {
    private val log = LoggerFactory.getLogger(JwtAuthorizationFilter::class.java)
  }
}