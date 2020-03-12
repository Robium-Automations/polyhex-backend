package com.robiumautomations.polyhex.services

import com.robiumautomations.polyhex.models.universityentities.University
import com.robiumautomations.polyhex.repos.UniversityRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UniversityService {

  @Autowired
  private lateinit var universityRepo: UniversityRepo

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
}