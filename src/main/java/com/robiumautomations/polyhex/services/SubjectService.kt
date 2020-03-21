package com.robiumautomations.polyhex.services

import com.robiumautomations.polyhex.models.universityentities.Subject
import com.robiumautomations.polyhex.repos.SubjectRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SubjectService {

  @Autowired
  private lateinit var subjectRepo: SubjectRepo

  @Autowired
  private lateinit var facultyService: FacultyService

  @Autowired
  private lateinit var userService: UserService

  fun createSubject(
      subjectName: String,
      subjectDescription: String? = null,
      facultyId: String
  ): Subject {
    if (facultyService.getFacultyById(facultyId) == null) {
      throw Exception("There is no faculty with faculty id: $facultyId.")
    }
    if (!isFacultyNameIsAvailable(facultyId, subjectName)) {
      throw Exception("There is subject with such name at the faculty.")
    }

    return Subject(
        subjectName = subjectName,
        subjectDescription = subjectDescription,
        facultyId = facultyId
    ).also {
      subjectRepo.save(it)
    }
  }

  private fun isFacultyNameIsAvailable(facultyId: String, subjectName: String): Boolean {
    return subjectRepo.checkIfSubjectNameAvailable(facultyId, subjectName).isEmpty()
  }

  fun getSubjectsByFacultyId(
      userId: String,
      subjectQuery: String?,
      facultyId: String,
      offset: Int = 0,
      limit: Int = 10
  ): List<Subject> {
    val user = userService.getUserInfo(userId) ?: throw Exception("No user with id: $userId found.")
    val universityId = user.universityId ?: throw Exception("User: $userId does not belongs to a university.")

    return if (subjectQuery == null) {
      subjectRepo.getSubjectsByFacultyId(facultyId, universityId, offset, limit)
    } else {
      subjectRepo.getSubjectsByFacultyId("%$subjectQuery%", facultyId, universityId, offset, limit)
    }
  }

  fun getSubjectsByUniversity(
      userId: String,
      subjectQuery: String?,
      offset: Int = 0,
      limit: Int = 10
  ): List<Subject> {
    val user = userService.getUserInfo(userId) ?: throw Exception("No user with id: $userId found.")
    val universityId = user.universityId ?: throw Exception("User: $userId does not belongs to a university.")

    return if (subjectQuery == null) {
      subjectRepo.getSubjects(universityId, offset, limit)
    } else {
      subjectRepo.getSubjects("%$subjectQuery%", universityId, offset, limit)
    }
  }
}