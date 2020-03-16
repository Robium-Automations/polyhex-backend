package com.robiumautomations.polyhex.repos

import com.robiumautomations.polyhex.models.UserId
import com.robiumautomations.polyhex.models.universityentities.Semester
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SemesterRepo : JpaRepository<Semester, String> {

  @Query(
      value = "SELECT * FROM semesters S WHERE S.university_id = (SELECT U.university_id FROM users U WHERE U.user_id = :userId )" +
          " LIMIT :limit OFFSET :offset ;",
      nativeQuery = true
  )
  fun getSemesters(
      userId: UserId,
      offset: Int = 0,
      limit: Int = 10
  ): List<Semester>

  @Query(
      value = "SELECT * FROM semesters S WHERE S.university_id = (SELECT U.university_id FROM users U WHERE U.user_id = :userId )" +
          " AND S.semester_name ILIKE :semester_name OR S.semester_description ILIKE :semester_description LIMIT :limit OFFSET :offset ;",
      nativeQuery = true
  )
  fun getSemesters(
      userId: UserId,
      semesterName: String,
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

  /**
   * Method is used to ensure that current [semesterId] exists and belongs to the user's [userId] university
   * @return listOf(1) if everything is fine, otherwise emptyList()
   */
  @Query(
      value = "SELECT 1 FROM semesters S JOIN users U ON S.university_id = U.university_id " +
          "WHERE S.semester_id = :semesterId AND U.user_id = :userId ;",
      nativeQuery = true
  )
  fun checkIfSemesterBelongsToUserUniversity(semesterId: String, userId: String): List<Int>
}