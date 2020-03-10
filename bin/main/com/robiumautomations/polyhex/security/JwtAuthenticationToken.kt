package com.robiumautomations.polyhex.security

import com.robiumautomations.polyhex.enums.UserRole
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(
    val token: String,
    val userId: String,
    val username: String,
    val userRole: UserRole,
    val authorities: List<GrantedAuthority>
) : UsernamePasswordAuthenticationToken(userId, null, authorities) {

  override fun getCredentials() = null

  override fun getPrincipal() = null
}