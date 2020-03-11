package com.robiumautomations.polyhex.repos

import com.robiumautomations.polyhex.models.universityentities.Semester
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SemesterRepo : JpaRepository<Semester, String> {

  @Query(
      value = "SELECT * FROM semesters S WHERE S.university_id = :universityId LIMIT :limit OFFSET :offset ;",
      nativeQuery = true
  )
  fun getSemesters(
      universityId: String,
      offset: Int = 0,
      limit: Int = 10
  ): List<Semester>

  @Query(
      value = "SELECT 1 FROM semesters S WHERE S.university_id = :universityId AND S.semester_name = :semesterName ;",
      nativeQuery = true
  )
  fun checkIfSemesterNameAvailable(universityId: String, semesterName: String): List<Int>

  @Query(
      value = "SELECT S.semester_id FROM semesters S WHERE S.university_id = :universityId AND S.semester_name = :semesterName ;",
      nativeQuery = true
  )
  fun getSemesterIdsByUniversityIdAndSemesterName(universityId: String, semesterName: String): List<String>
}