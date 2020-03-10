package com.robiumautomations.polyhex.security

import javax.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import javax.servlet.ServletException
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import com.robiumautomations.polyhex.security.utils.JwtUtils
import com.robiumautomations.polyhex.security.utils.SecurityConstants.HEADER_STRING
import com.robiumautomations.polyhex.security.utils.SecurityConstants.TOKEN_PREFIX

class JwtAuthorizationFilter(authManager: AuthenticationManager) : BasicAuthenticationFilter(authManager) {

  @Throws(IOException::class, ServletException::class)
  override fun doFilterInternal(req: HttpServletRequest,
      res: HttpServletResponse,
      chain: FilterChain) {
    val header = req.getHeader(HEADER_STRING)

    if (header == null || !header.startsWith(TOKEN_PREFIX)) {
      chain.doFilter(req, res)
      return
    }

    SecurityContextHolder.getContext().authentication = getAuthentication(req)
    chain.doFilter(req, res)
  }

  private fun getAuthentication(request: HttpServletRequest): JwtAuthenticationToken? {
    val token = request.getHeader(HEADER_STRING)
    JwtUtils.parseToken(token)?.let {
      return JwtAuthenticationToken(
          token,
          it.userId,
          it.username,
          it.role,
          it.authorities
      )
    }
    return null
  }
}