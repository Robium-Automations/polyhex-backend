package com.robiumautomations.polyhex.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class JwtAuthenticationToken(
    val token: String
) : UsernamePasswordAuthenticationToken(null, null) {

  override fun getCredentials() = null

  override fun getPrincipal() = null
}