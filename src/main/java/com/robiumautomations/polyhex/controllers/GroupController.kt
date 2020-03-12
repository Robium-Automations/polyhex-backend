package com.robiumautomations.polyhex.controllers

import com.robiumautomations.polyhex.dtos.studygroups.CreateGroupRequestDto
import com.robiumautomations.polyhex.dtos.studygroups.ManageGroupsRequestDto
import com.robiumautomations.polyhex.enums.ManageGroupAction
import com.robiumautomations.polyhex.enums.UserRole
import com.robiumautomations.polyhex.security.utils.AuthenticationUtils
import com.robiumautomations.polyhex.services.StudyGroupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
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

  @PostMapping(
      "/groups/{groupId}",
      consumes = [MediaType.APPLICATION_JSON_VALUE]
  )
  fun manageGroup(
      @PathVariable("groupId") groupId: String,
      @RequestBody dto: ManageGroupsRequestDto
  ): ResponseEntity<Any> {
    if (groupId.isBlank()) {
      return ResponseEntity(mapOf("Message" to "Specify 'groupId' property."), HttpStatus.BAD_REQUEST)
    }
    if (dto.action.isNullOrBlank()) {
      return ResponseEntity(mapOf("Message" to "Specify 'action' property."), HttpStatus.BAD_REQUEST)
    }
    val action: ManageGroupAction
    try {
      action = ManageGroupAction.valueOf(dto.action)
    } catch (e: IllegalArgumentException) {
      return ResponseEntity(
          mapOf("Message" to "Unknown action: '${dto.action}'. Possible actions: ${ManageGroupAction.values()}"),
          HttpStatus.BAD_REQUEST
      )
    }
    if (action == ManageGroupAction.remove
        && AuthenticationUtils.getCurrentUserRole() != UserRole.moderator) {
      return ResponseEntity(
          mapOf("Message" to "Only moderator can remove users from groups. Fuck off."),
          HttpStatus.FORBIDDEN
      )
    }
    return try {
      groupService.manageGroup(action, groupId, AuthenticationUtils.getCurrentUserId(), dto.userId)
      ResponseEntity.ok().build()
    } catch (e: Exception) {
      e.printStackTrace()
      ResponseEntity(mapOf("Message" to e.message), HttpStatus.BAD_REQUEST)
    }
  }
}