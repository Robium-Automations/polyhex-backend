package com.robiumautomations.polyhex.services

import com.robiumautomations.polyhex.models.universityentities.Faculty
import com.robiumautomations.polyhex.repos.FacultyRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FacultyService {

  @Autowired
  private lateinit var userService: UserService

  @Autowired
  private lateinit var facultyRepo: FacultyRepo

  fun createFaculty(facultyName: String, creatorId: String): Faculty {
    val universityId = userService.getUserInfo(creatorId)?.universityId
        ?: throw Exception("No universityId for creator: $creatorId")

    Faculty(
        facultyName = facultyName,
        universityId = universityId
    ).also {
      facultyRepo.save(it)
      return it
    }
  }

  fun getByFacultiesByUniversityId(universityId: String): List<Faculty> {
    return facultyRepo.getByUniversityId(universityId)
  }

  fun getFacultyById(facultyId: String): Faculty? {
    return facultyRepo.findByIdOrNull(facultyId)
  }
}