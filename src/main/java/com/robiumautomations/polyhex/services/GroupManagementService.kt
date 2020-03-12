package com.robiumautomations.polyhex.services

import com.robiumautomations.polyhex.models.UserId
import com.robiumautomations.polyhex.models.UsersGroups
import com.robiumautomations.polyhex.repos.UsersGroupsRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GroupManagementService {

  @Autowired
  private lateinit var usersGroupsRepo: UsersGroupsRepo

  fun joinGroup(groupId: String, currentUserId: UserId) {
    val usersGroups = usersGroupsRepo.getByUserIdAndGroupId(groupId, currentUserId)
    if (usersGroups != null) {
      throw Exception("User: $currentUserId is already member of the group: $groupId.")
    }
    UsersGroups(
        userId = currentUserId,
        groupId = groupId
    ).also {
      usersGroupsRepo.save(it)
    }
  }

  fun leaveGroup(groupId: String, currentUserId: String) {
    val usersGroups = usersGroupsRepo.getByUserIdAndGroupId(groupId, currentUserId)
        ?: throw Exception("User: $currentUserId is not a member of the group: $groupId.")
    usersGroupsRepo.deleteById(usersGroups.id)
  }

  fun removeUser(groupId: String, userId: UserId) {
    usersGroupsRepo.getByUserIdAndGroupId(groupId, userId)?.let {
      usersGroupsRepo.deleteById(it.id)
    } ?: throw Exception("There is no user: $userId in the group: $groupId.")
  }
}