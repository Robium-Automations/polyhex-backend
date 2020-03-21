package com.robiumautomations.polyhex.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

  @GetMapping("/helloworld", produces = [MediaType.APPLICATION_JSON_VALUE])
  fun helloWorld() = ResponseEntity(mapOf("Hello" to "world"), HttpStatus.OK)

  @GetMapping("/helloworld_protected", produces = [MediaType.APPLICATION_JSON_VALUE])
  fun helloWorldProtected() = ResponseEntity(mapOf("Hello" to "world"), HttpStatus.OK)

  @GetMapping("/health", produces = [MediaType.APPLICATION_JSON_VALUE])
  fun health() = ResponseEntity("This shit does work!!!", HttpStatus.OK)
}