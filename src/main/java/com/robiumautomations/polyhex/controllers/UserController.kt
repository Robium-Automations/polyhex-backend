package com.robiumautomations.polyhex.controllers

import com.robiumautomations.polyhex.dtos.users.RegistrationUser
import com.robiumautomations.polyhex.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController {

  @Autowired
  private lateinit var userService: UserService

  @PostMapping(
      "/users",
      consumes = [MediaType.APPLICATION_JSON_VALUE],
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun createUser(@RequestBody registrationUser: RegistrationUser): ResponseEntity<Any> {
    return try {
      ResponseEntity(userService.registerNewUser(registrationUser), HttpStatus.CREATED)
    } catch (e: Exception) {
      ResponseEntity(mapOf("Message" to e.message), HttpStatus.BAD_REQUEST)
    }
  }

  @GetMapping(
      "/users/{user_id}",
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun getUser(@PathVariable("user_id") userId: String): ResponseEntity<Any> {
    userService.getUserInfo(userId)?.let {
      return ResponseEntity.ok(it)
    }
    return ResponseEntity(mapOf("Message" to "User with id: $userId not found."), HttpStatus.NOT_FOUND)
  }
}