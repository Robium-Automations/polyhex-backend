package com.robiumautomations.polyhex.security

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationSuccessHandler : AuthenticationSuccessHandler {

  override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse?,
      authentication: Authentication?) {
    // do nothing
  }
}