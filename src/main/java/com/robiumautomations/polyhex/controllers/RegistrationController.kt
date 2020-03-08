package com.robiumautomations.polyhex.controllers

import com.robiumautomations.polyhex.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class RegistrationController {

  @Autowired
  private lateinit var userService: UserService

  @RequestMapping(
      "/usernames/{username}",
      method = [RequestMethod.HEAD]
  )
  fun isUsernameFree(@PathVariable("username") username: String): ResponseEntity<Any> {
    if (username.isBlank()) {
      return ResponseEntity(HttpStatus.BAD_REQUEST)
    }
    return if (userService.isUsernameFree(username)) {
      ResponseEntity(HttpStatus.NO_CONTENT)
    } else {
      ResponseEntity(HttpStatus.CONFLICT)
    }
  }

  @RequestMapping(
      "/emails/{email}",
      method = [RequestMethod.HEAD]
  )
  fun isEmailFree(@PathVariable("email") email: String): ResponseEntity<Any> {
    if (email.isBlank()) {
      return ResponseEntity(HttpStatus.BAD_REQUEST)
    }
    return if (userService.isEmailFree(email)) {
      ResponseEntity(HttpStatus.NO_CONTENT)
    } else {
      ResponseEntity(HttpStatus.CONFLICT)
    }
  }
}