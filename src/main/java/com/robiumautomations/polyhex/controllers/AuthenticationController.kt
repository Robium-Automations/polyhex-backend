package com.robiumautomations.polyhex.controllers

import com.robiumautomations.polyhex.models.UserCredentials
import com.robiumautomations.polyhex.security.CustomAuthentication
import com.robiumautomations.polyhex.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController {

  @Autowired
  private lateinit var customAuthentication: CustomAuthentication

  @Autowired
  private lateinit var userService: UserService

  @PostMapping("/signin")
  fun singIn(@RequestBody user: UserCredentials): ResponseEntity<Any> {
    if (user.username.isNullOrEmpty()) {
      return ResponseEntity("Specify username.", HttpStatus.BAD_REQUEST)
    }
    if (user.password.isNullOrEmpty()) {
      return ResponseEntity("Specify password.", HttpStatus.BAD_REQUEST)
    }
    return try {
      return ResponseEntity.ok()
          .header("Authorization", customAuthentication.attemptAuthentication(user.username, user.password))
          .body(userService.getUserInfoByUsername(user.username))
    } catch (e: AuthenticationException) {
      e.printStackTrace()
      ResponseEntity(mapOf("Message" to "Invalid credentials."), HttpStatus.BAD_REQUEST)
    }
  }
}