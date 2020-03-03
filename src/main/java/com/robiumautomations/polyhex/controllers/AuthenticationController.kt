package com.robiumautomations.polyhex.controllers

import com.robiumautomations.polyhex.models.UserCredentials
import com.robiumautomations.polyhex.security.CustomAuthentication
import com.robiumautomations.polyhex.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*

@RestController
class AuthenticationController {

  @Autowired
  private lateinit var customAuthentication: CustomAuthentication

  @PostMapping("/api/public/login",
      consumes = [MediaType.APPLICATION_JSON_VALUE],
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun singIn(@RequestBody user: UserCredentials): ResponseEntity<String> {
    val token: String
    try {
      token = customAuthentication.attemptAuthentication(user.username!!, user.password!!)
    } catch (e: AuthenticationException) {
      e.printStackTrace()
      return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    val headers = HttpHeaders()
    headers.add("Authorization", token)
    return ResponseEntity(headers, HttpStatus.OK)
  }
}