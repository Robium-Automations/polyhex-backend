package com.robiumautomations.polyhex.controllers

import com.robiumautomations.polyhex.dtos.subjects.CreateSubjectRequestDto
import com.robiumautomations.polyhex.enums.UserRole
import com.robiumautomations.polyhex.security.utils.AuthenticationUtils
import com.robiumautomations.polyhex.services.SubjectService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class SubjectController {

  @Autowired
  private lateinit var subjectService: SubjectService

  @PostMapping(
      "/subjects",
      consumes = [MediaType.APPLICATION_JSON_VALUE],
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun createSubject(@RequestBody subject: CreateSubjectRequestDto): ResponseEntity<Any> {
    if (AuthenticationUtils.getCurrentUserRole() != UserRole.moderator) {
      return ResponseEntity(HttpStatus.FORBIDDEN)
    }
    if (subject.subjectName.isNullOrBlank()) {
      return ResponseEntity(mapOf("Message" to "Specify 'subjectName' property."), HttpStatus.BAD_REQUEST)
    }
    if (subject.facultyId.isNullOrBlank()) {
      return ResponseEntity(mapOf("Message" to "Specify 'facultyId' property."), HttpStatus.BAD_REQUEST)
    }
    return try {
      ResponseEntity.ok(
          subjectService.createSubject(subject.subjectName, subject.subjectDescription, subject.facultyId)
      )
    } catch (e: Exception) {
      e.printStackTrace()
      ResponseEntity(mapOf("Message" to e.message), HttpStatus.BAD_REQUEST)
    }
  }

  @GetMapping(
      "/faculties/{facultyId}/subjects",
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun getUniversityFaculties(
      @PathVariable("facultyId") facultyId: String,
      @RequestParam("offset", required = false, defaultValue = "0") offset: String,
      @RequestParam("limit", required = false, defaultValue = "10") limit: String
  ): ResponseEntity<Any> {
    if (facultyId.isBlank()) {
      return ResponseEntity(mapOf("Message" to "Specify 'facultyId'."), HttpStatus.BAD_REQUEST)
    }
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
    return with(HttpHeaders()) {
      add("offset", offset)
      add("limit", limit)
      ResponseEntity(
          subjectService.getSubjectsByFacultyId(facultyId, offset.toInt(), limit.toInt()),
          this,
          HttpStatus.OK
      )
    }
  }
}