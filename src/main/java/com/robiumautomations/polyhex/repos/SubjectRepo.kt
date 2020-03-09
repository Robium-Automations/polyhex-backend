package com.robiumautomations.polyhex.repos

import com.robiumautomations.polyhex.models.universityentities.Subject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SubjectRepo : JpaRepository<Subject, String> {

  @Query(
      value = "SELECT 1 FROM subjects S WHERE S.faculty_id = :facultyId AND S.subject_name = :subjectName ;",
      nativeQuery = true
  )
  fun checkIfSubjectNameAvailable(facultyId: String, subjectName: String): List<Int>

  @Query(
      value = "SELECT * FROM subjects S WHERE S.faculty_id = :facultyId LIMIT :limit OFFSET :offset ;",
      nativeQuery = true
  )
  fun getByFacultyId(
      facultyId: String,
      offset: Int,
      limit: Int
  ): List<Subject>
}