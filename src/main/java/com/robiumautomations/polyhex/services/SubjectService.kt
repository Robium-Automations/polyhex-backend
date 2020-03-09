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

    Subject(
        subjectName = subjectName,
        subjectDescription = subjectDescription,
        facultyId = facultyId
    ).also {
      subjectRepo.save(it)
      return it
    }
  }

  private fun isFacultyNameIsAvailable(facultyId: String, subjectName: String): Boolean {
    return subjectRepo.checkIfSubjectNameAvailable(facultyId, subjectName).isEmpty()
  }

  fun getSubjectsByFacultyId(
      facultyId: String,
      offset: Int = 0,
      limit: Int = 10
  ): List<Subject> {
    return subjectRepo.getByFacultyId(facultyId, offset, limit)
  }
}