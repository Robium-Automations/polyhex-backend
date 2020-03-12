package com.robiumautomations.polyhex.services

import com.robiumautomations.polyhex.enums.ManageGroupAction
import com.robiumautomations.polyhex.models.StudyGroup
import com.robiumautomations.polyhex.models.UserId
import com.robiumautomations.polyhex.repos.SemesterRepo
import com.robiumautomations.polyhex.repos.StudyGroupRepo
import com.robiumautomations.polyhex.repos.SubjectRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StudyGroupService {

  @Autowired
  private lateinit var studyGroupRepo: StudyGroupRepo

  @Autowired
  private lateinit var semesterRepo: SemesterRepo

  @Autowired
  private lateinit var subjectRepo: SubjectRepo

  @Autowired
  private lateinit var groupManagementService: GroupManagementService

  fun createGroup(
      groupName: String,
      semesterId: String,
      subjectId: String,
      creatorId: String
  ): StudyGroup {
    ensureSemesterIdIsCorrect(semesterId, creatorId)
    ensureSubjectIdIsCorrect(subjectId, creatorId)
    ensureGroupNameIsAvailable(groupName, subjectId, semesterId)

    return StudyGroup(
        studyGroupName = groupName,
        subjectId = subjectId,
        semesterId = semesterId
    ).also {
      studyGroupRepo.save(it)
    }
  }

  fun manageGroup(
      action: ManageGroupAction,
      groupId: String,
      currentUserId: String,
      userId: UserId? = null
  ) {
    studyGroupRepo.findGroupByGroupIdAndUsersUniversity(groupId, currentUserId)
        ?: throw Exception("No group with id: $groupId.")

    when (action) {
      ManageGroupAction.join -> groupManagementService.joinGroup(groupId, currentUserId)

      ManageGroupAction.leave -> groupManagementService.leaveGroup(groupId, currentUserId)

      ManageGroupAction.remove -> {
        userId?.let { groupManagementService.removeUser(groupId, it) }
            ?: throw Exception("Property 'userId' is not specified. Dude, I don't know whom I should delete.")
      }
    }
  }

  /**
   * @throws [Exception] if:
   *  1. current [semesterId] does not exists in DB
   *  2. [semesterId] does not belong to the creator's [creatorId] university or
   */
  private fun ensureSemesterIdIsCorrect(semesterId: String, creatorId: String) {
    if (semesterRepo.checkIfSemesterBelongsToUserUniversity(semesterId, creatorId).isEmpty()) {
      throw Exception("Incorrect semester id: $semesterId")
    }
  }

  /**
   * @throws [Exception] if:
   *  1. current [subjectId] does not exists in DB
   *  2. [subjectId] does not belong to the creator's [creatorId] university or
   */
  private fun ensureSubjectIdIsCorrect(subjectId: String, creatorId: String) {
    if (subjectRepo.checkIfSubjectBelongsToUserUniversity(subjectId, creatorId).isEmpty()) {
      throw Exception("Incorrect subjectId id: '$subjectId")
    }
  }

  /**
   * @throws [Exception] if [groupName] is already in use for the given [subjectId] and [semesterId]
   */
  private fun ensureGroupNameIsAvailable(groupName: String, subjectId: String, semesterId: String) {
    if (studyGroupRepo.checkIfGroupNameIsAvailable(groupName, subjectId, semesterId).isNotEmpty()) {
      throw Exception("Group name '$groupName' is not available. Please choose another one.")
    }
  }
}