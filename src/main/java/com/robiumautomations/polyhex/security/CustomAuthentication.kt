package com.robiumautomations.polyhex.security

import com.robiumautomations.polyhex.security.utils.JwtUtils
import com.robiumautomations.polyhex.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component

@Component
class CustomAuthentication {

  @Autowired
  private lateinit var userService: UserService

  @Autowired
  private lateinit var jwtUtils: JwtUtils

  @Throws(AuthenticationException::class)
  fun attemptAuthentication(username: String, password: String): String {
    val checkedUser = userService.getByUsername(username)
    return if (checkedUser != null && checkedUser.password == JwtUtils.hashPassword(password)) {
      jwtUtils.generateToken(checkedUser)
    } else {
      throw BadCredentialsException("Incorrect credentials: username: $username and password: $password")
    }
  }
}