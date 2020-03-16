package com.robiumautomations.polyhex.services

import com.robiumautomations.polyhex.daos.StudyGroupRepoDao
import com.robiumautomations.polyhex.enums.ManageGroupAction
import com.robiumautomations.polyhex.models.StudyGroup
import com.robiumautomations.polyhex.models.User
import com.robiumautomations.polyhex.models.UserId
import com.robiumautomations.polyhex.repos.SemesterRepo
import com.robiumautomations.polyhex.repos.StudyGroupRepo
import com.robiumautomations.polyhex.repos.SubjectRepo
import com.robiumautomations.polyhex.repos.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StudyGroupService @Autowired constructor(
    private val groupManagementService: GroupManagementService,
    private val studyGroupRepoDao: StudyGroupRepoDao,
    private val studyGroupRepo: StudyGroupRepo,
    private val semesterRepo: SemesterRepo,
    private val subjectRepo: SubjectRepo,
    private val userRepo: UserRepo
) {

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

  @JvmOverloads
  fun searchGroups(
      userId: UserId,
      facultyId: String? = null,
      subjectId: String? = null,
      semesterId: String? = null,
      groupName: String? = null,
      subjectName: String? = null,
      joined: Boolean = false,
      offset: Int = 0,
      limit: Int = 0
  ): List<StudyGroup> {
    return studyGroupRepoDao.getGroups(
        userId, facultyId, subjectId, semesterId, groupName, subjectName, joined, offset, limit
    )
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

  fun getUsersOfGroup(
      groupId: String,
      name: String? = null,
      currentUserId: UserId
  ): List<User> {
    return if (name == null) {
      userRepo.getUsersOfGroup(studyGroupId = groupId, currentUserId = currentUserId)
    } else {
      userRepo.getUsersOfGroup(studyGroupId = groupId, currentUserId = currentUserId, nameQuery = "%$name%")
    }
  }
}