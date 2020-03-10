package com.robiumautomations.polyhex.security.utils

import org.springframework.security.core.context.SecurityContextHolder
import com.robiumautomations.polyhex.security.JwtAuthenticationToken

object AuthenticationUtils {

  private fun getCurrentAuthentication(): JwtAuthenticationToken {
    return SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
  }

  fun getCurrentUserId() = getCurrentAuthentication().userId

  fun getCurrentUsername() = getCurrentAuthentication().username

  fun getCurrentUserRole() = getCurrentAuthentication().userRole
}