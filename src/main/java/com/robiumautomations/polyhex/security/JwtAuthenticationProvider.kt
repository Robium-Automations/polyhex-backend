package com.robiumautomations.polyhex.security

import com.robiumautomations.polyhex.security.utils.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider : AbstractUserDetailsAuthenticationProvider() {

  @Autowired
  private lateinit var jwtUtils: JwtUtils

  override fun supports(authentication: Class<*>): Boolean {
    return JwtAuthenticationToken::class.java.isAssignableFrom(authentication)
  }

  @Throws(AuthenticationException::class)
  override fun additionalAuthenticationChecks(userDetails: UserDetails,
      authentication: UsernamePasswordAuthenticationToken) {
  }

  @Throws(AuthenticationException::class)
  override fun retrieveUser(
      username: String,
      authentication: UsernamePasswordAuthenticationToken
  ): UserDetails? {

    val token = (authentication as JwtAuthenticationToken).token
    val parsedUser = jwtUtils.parseToken(token)

    return parsedUser?.let {
      val authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(parsedUser.role)
      AuthenticationUser(
          userId = parsedUser.userId,
          username = parsedUser.username,
          authorities = authorityList,
          role = parsedUser.role
      )
    }
  }
}