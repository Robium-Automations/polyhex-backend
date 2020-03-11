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

    // TODO(check if there is university with such universityId)

    if (!isFacultyNameIsAvailable(universityId, facultyName)) {
      throw Exception("There is faculty with such name at the university.")
    }

    return Faculty(
        facultyName = facultyName,
        universityId = universityId
    ).also {
      facultyRepo.save(it)
    }
  }

  private fun isFacultyNameIsAvailable(universityId: String, facultyName: String): Boolean {
    return facultyRepo.checkIfFacultyNameAvailable(universityId, facultyName).isEmpty()
  }

  fun getByFacultiesByUniversityId(
      universityId: String,
      offset: Int = 0,
      limit: Int = 10
  ): List<Faculty> {
    return facultyRepo.getByUniversityId(universityId, offset, limit)
  }

  fun getFacultyById(facultyId: String): Faculty? {
    return facultyRepo.findByIdOrNull(facultyId)
  }
}