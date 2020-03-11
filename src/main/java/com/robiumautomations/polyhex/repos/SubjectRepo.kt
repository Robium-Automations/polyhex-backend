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

  /**
   * Method is used to ensure that current [subjectId] exists and belongs to the user's [userId] university
   * @return listOf(1) if everything is fine, otherwise emptyList()
   */
  @Query(
      value = "SELECT 1 WHERE (SELECT F.university_id FROM subjects S JOIN faculties F ON S.faculty_id = F.faculty_id " +
          "WHERE S.subject_id = :subjectId ) = (SELECT U.university_id FROM users U WHERE U.user_id = :userId );",
      nativeQuery = true
  )
  fun checkIfSubjectBelongsToUserUniversity(subjectId: String, userId: String): List<Int>
}