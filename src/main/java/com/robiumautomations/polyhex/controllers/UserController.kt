package com.robiumautomations.polyhex.controllers

import com.robiumautomations.polyhex.models.User
import com.robiumautomations.polyhex.models.dtos.users.RegistrationUser
import com.robiumautomations.polyhex.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

  @Autowired
  private lateinit var userService: UserService

  @PostMapping(
      "/users",
      consumes = [MediaType.APPLICATION_JSON_VALUE],
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun createUser(@RequestBody registrationUser: RegistrationUser): ResponseEntity<User> {
    try {
      userService.registerNewUser(registrationUser).also {
        return ResponseEntity(it, HttpStatus.CREATED)
      }
    } catch (e: Exception) {
      HttpHeaders().let {
        it.add("Message", e.message)
        return ResponseEntity(it, HttpStatus.BAD_REQUEST)
      }
    }
  }
}