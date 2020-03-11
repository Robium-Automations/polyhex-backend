package com.robiumautomations.polyhex.controllers

import com.robiumautomations.polyhex.dtos.semesters.CreateSemesterRequestDto
import com.robiumautomations.polyhex.enums.UserRole
import com.robiumautomations.polyhex.security.utils.AuthenticationUtils
import com.robiumautomations.polyhex.services.SemesterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class SemesterController {

  @Autowired
  private lateinit var semesterService: SemesterService

  @PostMapping(
      "/semesters",
      consumes = [MediaType.APPLICATION_JSON_VALUE],
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun createSemester(@RequestBody createSemesterRequestDto: CreateSemesterRequestDto): ResponseEntity<Any> {
    if (AuthenticationUtils.getCurrentUserRole() != UserRole.moderator) {
      return ResponseEntity(HttpStatus.FORBIDDEN)
    }
    if (createSemesterRequestDto.semesterName.isNullOrBlank()) {
      return ResponseEntity(mapOf("Message" to "Specify 'semesterName' property."), HttpStatus.BAD_REQUEST)
    }
    return try {
      ResponseEntity.ok(
          semesterService.createSemester(
              semesterName = createSemesterRequestDto.semesterName,
              semesterDescription = createSemesterRequestDto.semesterDescription,
              startDate = createSemesterRequestDto.startDate,
              endDate = createSemesterRequestDto.endDate,
              creatorId = AuthenticationUtils.getCurrentUserId()
          )
      )
    } catch (e: Exception) {
      e.printStackTrace()
      ResponseEntity(mapOf("Message" to e.message), HttpStatus.BAD_REQUEST)
    }
  }

  @GetMapping(
      "/universities/{universityId}/semesters",
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun getSemesters(
      @PathVariable("universityId") universityId: String,
      @RequestParam("offset", required = false, defaultValue = "0") offset: String,
      @RequestParam("limit", required = false, defaultValue = "10") limit: String
  ): ResponseEntity<Any> {
    if (universityId.isBlank()) {
      return ResponseEntity(mapOf("Message" to "Specify 'universityId'."), HttpStatus.BAD_REQUEST)
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
    return ResponseEntity.ok()
        .header("offset", offset)
        .header("limit", limit)
        .body(semesterService.getSemester(universityId, offset.toInt(), limit.toInt()))
  }

  @PutMapping(
      "/semesters/{semesterId}",
      consumes = [MediaType.APPLICATION_JSON_VALUE],
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun updateSemester(
      @PathVariable("semesterId") semesterId: String,
      @RequestBody createSemesterRequestDto: CreateSemesterRequestDto
  ): ResponseEntity<Any> {
    if (AuthenticationUtils.getCurrentUserRole() != UserRole.moderator) {
      return ResponseEntity(HttpStatus.FORBIDDEN)
    }
    if (createSemesterRequestDto.semesterName.isNullOrBlank()) {
      return ResponseEntity(mapOf("Message" to "Specify 'semesterName' property."), HttpStatus.BAD_REQUEST)
    }
    try {
      return ResponseEntity.ok().body(
          semesterService.updateSemester(
              semesterId = semesterId,
              semesterName = createSemesterRequestDto.semesterName,
              semesterDescription = createSemesterRequestDto.semesterDescription,
              startDate = createSemesterRequestDto.startDate,
              endDate = createSemesterRequestDto.endDate
          )
      )
    } catch (e: Exception) {
      e.printStackTrace()
      return ResponseEntity(mapOf("Message" to e.message), HttpStatus.BAD_REQUEST)
    }
  }
}