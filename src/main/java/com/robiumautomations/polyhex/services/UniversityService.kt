package com.robiumautomations.polyhex.services

import com.robiumautomations.polyhex.enums.ManageUniversityAction
import com.robiumautomations.polyhex.models.User
import com.robiumautomations.polyhex.models.universityentities.University
import com.robiumautomations.polyhex.repos.UniversityRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UniversityService @Autowired constructor(
    private val universityRepo: UniversityRepo,
    private val userService: UserService
) {

  fun getUserUniversity(userId: String): University? {
    return universityRepo.getUserUniversity(userId)
  }

  fun getUniversities(
      query: String? = null,
      offset: Int = 0,
      limit: Int = 10
  ): List<University> {
    return query?.let {
      universityRepo.getUniversities("%$it%", offset, limit)
    } ?: universityRepo.getUniversities(offset, limit)
  }

  fun manageUniversity(
      action: ManageUniversityAction,
      userId: String,
      universityId: String
  ) {
    val user = userService.getUserInfo(userId) ?: throw Exception("No user with id: $userId.")
    when (action) {
      ManageUniversityAction.join -> {
        joinUniversity(user, universityId)
      }
    }
  }

  private fun joinUniversity(user: User, universityId: String) {
    if (user.universityId != null) {
      throw Exception("User: ${user.userId} already joined university: ${user.universityId}.")
    }
    universityRepo.findByIdOrNull(universityId)
        ?: throw Exception("No university with id: $universityId.")

    user.universityId = universityId
    userService.save(user)
  }

  fun getUniversityById(universityId: String): University? {
    return universityRepo.findByIdOrNull(universityId)
  }
}