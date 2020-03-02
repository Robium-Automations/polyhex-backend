package com.robiumautomations.polyhex.security.utils

import org.springframework.security.core.context.SecurityContextHolder
import com.robiumautomations.polyhex.security.AuthenticationUser



object AuthenticationUtils {

  fun getCurrentAuthenticationUser(): AuthenticationUser {
    return SecurityContextHolder.getContext().authentication.principal as AuthenticationUser
  }

  fun getCurrentUserId(): String {
    return getCurrentAuthenticationUser().userId
  }

  fun getCurrentUsername(): String {
    return getCurrentAuthenticationUser().username
  }

}