package com.robiumautomations.polyhex.controllers

import com.robiumautomations.polyhex.enums.UserRole
import com.robiumautomations.polyhex.models.universityentities.Faculty
import com.robiumautomations.polyhex.security.utils.AuthenticationUtils
import com.robiumautomations.polyhex.services.FacultyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class FacultyController {

  @Autowired
  private lateinit var facultyService: FacultyService

  @PostMapping(
      "/faculties",
      consumes = [MediaType.APPLICATION_JSON_VALUE],
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun createFaculty(@RequestBody faculty: Faculty): ResponseEntity<Any> {
    if (AuthenticationUtils.getCurrentUserRole() != UserRole.moderator) {
      return ResponseEntity(HttpStatus.FORBIDDEN)
    }
//    facultyService

    return ResponseEntity.ok("")
  }
}