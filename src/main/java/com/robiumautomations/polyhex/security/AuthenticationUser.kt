package com.robiumautomations.polyhex.security

import com.robiumautomations.polyhex.enums.UserRole
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

class AuthenticationUser(
    val userId: String,
    private val username: String,
    private val authorities: List<GrantedAuthority>,
    val role: UserRole,
    private val password: String? = null,
    private val accountNonExpired: Boolean = false,
    private val accountNonLocked: Boolean = false,
    private val credentialsNonExpired: Boolean = false,
    private val enabled: Boolean = false
) : UserDetails {

  override fun getUsername() = username

  override fun getPassword() = password

  override fun getAuthorities() = authorities

  override fun isEnabled() = enabled

  override fun isCredentialsNonExpired() = credentialsNonExpired

  override fun isAccountNonExpired() = accountNonExpired

  override fun isAccountNonLocked() = accountNonLocked
}