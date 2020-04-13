package com.robiumautomations.polyhex.controllers

import com.robiumautomations.polyhex.dtos.users.RegistrationUser
import com.robiumautomations.polyhex.dtos.users.UpdatedUser
import com.robiumautomations.polyhex.security.utils.AuthenticationUtils
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

  @PutMapping(
      "/users",
      consumes = [MediaType.APPLICATION_JSON_VALUE],
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun updateUser(@RequestBody updatedUser: UpdatedUser): ResponseEntity<*> {
    return try {
      ResponseEntity(userService.updateUser(updatedUser), HttpStatus.OK)
    } catch (e: Exception) {
      ResponseEntity(mapOf("Message" to e.message), HttpStatus.BAD_REQUEST)
    }
  }

  @GetMapping(
      "/users/{user_id}",
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun getUser(@PathVariable("user_id") userId: String): ResponseEntity<Any> {
    userService.getUserWithGroups(userId)?.let {
      return ResponseEntity.ok(it)
    }
    return ResponseEntity(mapOf("Message" to "User with id: $userId not found."), HttpStatus.NOT_FOUND)
  }

  @DeleteMapping(
      "/users"
  )
  fun deleteUser(): ResponseEntity<Any> {
    userService.removeUser(AuthenticationUtils.getCurrentUserId())
    return ResponseEntity.ok().build()
  }

  @GetMapping(
      "/users",
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun getUsers(
      @RequestParam("name", required = false, defaultValue = "") name: String,
      @RequestParam("offset", required = false, defaultValue = "0") offset: String,
      @RequestParam("limit", required = false, defaultValue = "10") limit: String
  ): ResponseEntity<Any> {
    try {
      offset.toInt()
    } catch (e: NumberFormatException) {
      return ResponseEntity(mapOf("Message" to "Parameter 'offset' is not a number."), HttpStatus.BAD_REQUEST)
    }
    try {
      limit.toInt()
    } catch (e: NumberFormatException) {
      return ResponseEntity(mapOf("Message" to "Parameter 'limit' is not a number."), HttpStatus.BAD_REQUEST)
    }
    return ResponseEntity.ok()
        .header("limit", limit)
        .header("offset", offset)
        .body(
            userService.getUsers(
                if (name.isBlank()) null else name.trim(),
                offset.toInt(),
                limit.toInt(),
                AuthenticationUtils.getCurrentUserId()
            )
        )
  }

  @GetMapping(
      "/all_users",
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun getAllUsers() = ResponseEntity.ok().body(userService.getAll())
}