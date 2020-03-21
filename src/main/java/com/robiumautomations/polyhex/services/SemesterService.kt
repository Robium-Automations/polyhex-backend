package com.robiumautomations.polyhex.services

import com.robiumautomations.polyhex.models.UserId
import com.robiumautomations.polyhex.models.universityentities.Semester
import com.robiumautomations.polyhex.repos.SemesterRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class SemesterService {

  @Autowired
  private lateinit var semesterRepo: SemesterRepo

  @Autowired
  private lateinit var uniService: UniversityService

  fun createSemester(
      semesterName: String,
      semesterDescription: String? = null,
      startDate: Date? = null,
      endDate: Date? = null,
      creatorId: String
  ): Semester {
    val university = uniService.getUserUniversity(creatorId)
        ?: throw Exception("No universityId for creator: $creatorId")

    ensureSemesterNameIsAvailable(university.universityId!!, semesterName)

    return Semester(
        semesterName = semesterName,
        semesterDescription = semesterDescription,
        startDate = startDate,
        endDate = endDate,
        universityId = university.universityId
    ).also {
      semesterRepo.save(it)
    }
  }

  fun getSemester(
      userId: UserId,
      semesterName: String? = null,
      offset: Int = 0,
      limit: Int = 10
  ): List<Semester> {
    return if (semesterName == null) {
      semesterRepo.getSemesters(userId, offset, limit)
    } else {
      semesterRepo.getSemesters(userId, "%$semesterName%", offset, limit)
    }
  }

  fun updateSemester(
      semesterId: String,
      semesterName: String,
      semesterDescription: String? = null,
      startDate: Date? = null,
      endDate: Date? = null
  ): Semester {
    val semester = semesterRepo.findByIdOrNull(semesterId)
        ?: throw Exception("There is no semester with id: $semesterId.")

    ensureSemesterNameIsAvailableForUpdate(semester.universityId!!, semesterName, semesterId)

    return Semester(
        semesterId = semesterId,
        semesterName = semesterName,
        semesterDescription = semesterDescription,
        startDate = startDate,
        endDate = endDate,
        universityId = semester.universityId
    ).also {
      semesterRepo.save(it)
    }
  }

  private fun ensureSemesterNameIsAvailable(universityId: String, semesterName: String) {
    if (semesterRepo.checkIfSemesterNameAvailable(universityId, semesterName).isNotEmpty()) {
      throw Exception("There is semester with such name: $semesterName")
    }
  }

  private fun ensureSemesterNameIsAvailableForUpdate(universityId: String, semesterName: String, semesterId: String) {
    with(semesterRepo.getSemesterIdsByUniversityIdAndSemesterName(universityId, semesterName)) {
      if (isNotEmpty() && first() != semesterId) {
        throw Exception("There is semester with such name: $semesterName")
      }
    }
  }
}