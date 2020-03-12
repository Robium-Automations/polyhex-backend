package com.robiumautomations.polyhex.controllers

import com.robiumautomations.polyhex.dtos.universities.ManageUniversityRequestDto
import com.robiumautomations.polyhex.enums.ManageUniversityAction
import com.robiumautomations.polyhex.security.utils.AuthenticationUtils
import com.robiumautomations.polyhex.services.UniversityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UniversityController {

  @Autowired
  private lateinit var universityService: UniversityService

  @GetMapping(
      "/universities",
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun getUniversities(
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
            universityService.getUniversities(
                name.getQueryString(),
                offset.toInt(),
                limit.toInt()
            )
        )
  }

  @PutMapping(
      "/universities/{universityId}",
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun manageUniversity(
      @PathVariable("universityId") universityId: String,
      @RequestBody dto: ManageUniversityRequestDto
  ): ResponseEntity<Any> {
    if (dto.action.isNullOrBlank()) {
      return ResponseEntity(mapOf("Message" to "Specify 'action' property."), HttpStatus.BAD_REQUEST)
    }
    return try {
      universityService.manageUniversity(
          ManageUniversityAction.valueOf(dto.action),
          AuthenticationUtils.getCurrentUserId(),
          universityId
      )
      ResponseEntity.ok().build()
    } catch (e: IllegalArgumentException) {
      e.printStackTrace()
      ResponseEntity(
          mapOf("Message" to "Unknown action: '${dto.action}'. Possible actions: ${ManageUniversityAction.values()}"),
          HttpStatus.BAD_REQUEST
      )
    } catch (e: Exception) {
      e.printStackTrace()
      ResponseEntity(
          mapOf("Message" to e.message),
          HttpStatus.BAD_REQUEST
      )
    }
  }
}

private fun String.getQueryString() = if (isBlank()) null else this
