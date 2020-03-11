package com.robiumautomations.polyhex.controllers

import com.robiumautomations.polyhex.dtos.studygroups.CreateGroupRequestDto
import com.robiumautomations.polyhex.enums.UserRole
import com.robiumautomations.polyhex.security.utils.AuthenticationUtils
import com.robiumautomations.polyhex.services.StudyGroupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GroupController {

  @Autowired
  private lateinit var groupService: StudyGroupService

  @PostMapping(
      "/groups",
      consumes = [MediaType.APPLICATION_JSON_VALUE],
      produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun createGroup(@RequestBody dto: CreateGroupRequestDto): ResponseEntity<Any> {
    if (AuthenticationUtils.getCurrentUserRole() != UserRole.moderator) {
      return ResponseEntity(HttpStatus.FORBIDDEN)
    }
    if (dto.groupName.isNullOrBlank()) {
      return ResponseEntity(mapOf("Message" to "Specify 'groupName' property."), HttpStatus.BAD_REQUEST)
    }
    if (dto.semesterId.isNullOrBlank()) {
      return ResponseEntity(mapOf("Message" to "Specify 'semesterId' property."), HttpStatus.BAD_REQUEST)
    }
    if (dto.subjectId.isNullOrBlank()) {
      return ResponseEntity(mapOf("Message" to "Specify 'subjectId' property."), HttpStatus.BAD_REQUEST)
    }
    return try {
      ResponseEntity.ok(
          groupService.createGroup(
              groupName = dto.groupName,
              semesterId = dto.semesterId,
              subjectId = dto.subjectId,
              creatorId = AuthenticationUtils.getCurrentUserId()
          )
      )
    } catch (e: Exception) {
      e.printStackTrace()
      ResponseEntity(mapOf("Message" to e.message), HttpStatus.BAD_REQUEST)
    }
  }
}