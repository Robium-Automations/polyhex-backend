package com.robiumautomations.polyhex.controllers

import com.robiumautomations.polyhex.enums.UserRole
import com.robiumautomations.polyhex.dtos.faculties.CreateFacultyRequestDto
import com.robiumautomations.polyhex.security.utils.AuthenticationUtils
import com.robiumautomations.polyhex.services.FacultyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Exception

@RestController
class FacultyController {

  @Autowired
  private lateinit var facultyService: FacultyService

  @PostMapping(
      "/faculties",
      consumes = [MediaType.APPLICATION_JSON_VALUE],
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun createFaculty(@RequestBody faculty: CreateFacultyRequestDto): ResponseEntity<Any> {
    if (AuthenticationUtils.getCurrentUserRole() != UserRole.moderator) {
      return ResponseEntity(HttpStatus.FORBIDDEN)
    }
    if (faculty.facultyName.isNullOrBlank()) {
      return ResponseEntity(mapOf("Message" to "Specify 'facultyName' property."), HttpStatus.BAD_REQUEST)
    }
    return try {
      ResponseEntity.ok(
          facultyService.createFaculty(
              faculty.facultyName,
              AuthenticationUtils.getCurrentUserId()
          )
      )
    } catch (e: Exception) {
      e.printStackTrace()
      ResponseEntity(mapOf("Message" to e.message), HttpStatus.BAD_REQUEST)
    }
  }

  @GetMapping(
      "/universities/{universityId}/faculties",
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun getUniversityFaculties(
      @PathVariable("universityId") universityId: String,
      @RequestParam("offset", required = false, defaultValue = "0") offset: String,
      @RequestParam("limit", required = false, defaultValue = "10") limit: String
  ): ResponseEntity<Any> {
    if (universityId.isBlank()) {
      return ResponseEntity(mapOf("Message" to "Specify 'universityId'."), HttpStatus.BAD_REQUEST)
    }

    try {
      offset!!.toInt()
    } catch (e: NumberFormatException) {
      return ResponseEntity(mapOf("Message" to "Parameter 'offset' is not a number."), HttpStatus.BAD_REQUEST)
    }
    try {
      limit!!.toInt()
    } catch (e: NumberFormatException) {
      return ResponseEntity(mapOf("Message" to "Parameter 'limit' is not a number."), HttpStatus.BAD_REQUEST)
    }
    return with(HttpHeaders()) {
      add("offset", offset)
      add("limit", limit)
      ResponseEntity(
          facultyService.getByFacultiesByUniversityId(universityId, offset.toInt(), limit.toInt()),
          this,
          HttpStatus.OK
      )
    }
  }

  @GetMapping(
      "/faculties/{facultyId}",
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun getFaculty(
      @PathVariable("facultyId") facultyId: String
  ): ResponseEntity<Any> {
    if (facultyId.isBlank()) {
      return ResponseEntity(mapOf("Message" to "Specify 'facultyId'."), HttpStatus.BAD_REQUEST)
    }
    facultyService.getFacultyById(facultyId)?.let {
      return ResponseEntity.ok(it)
    }
    return ResponseEntity(HttpStatus.NO_CONTENT)
  }
}